package com.zl.es.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zl.es.client.EsClient;
import com.zl.es.model.Blog;
import com.zl.es.service.EsService;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * <p> 类描述：
 * <p> 创建人: zhangliang
 * <p> 创建时间: 2018/12/7 4:11 PM
 * <p> 版权申明：Huobi All Rights Reserved
 */
@Service("esService")
public class EsServiceimpl implements EsService {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private EsClient esClient;
    //索引库名
    @Value("${elasticsearch.indexName}")
    public String indexName;
    //类型名称
    @Value("${elasticsearch.type}")
    public String type;


    @Override
    public void queryDocument() {
        GetResponse getResponse = esClient.getClient().prepareGet(indexName,type,"4").get();
        Blog blog = JSONObject.parseObject(getResponse.getSourceAsString(),Blog.class);
        log.info("查询结果信息:{}",blog);
    }

    @Override
    public void multiSearch() {
        SearchResponse searchResponse = esClient.getClient().prepareSearch("index1","index2")
                .setTypes("type1","type2")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("multi","test"))
                .setPostFilter(QueryBuilders.boolQuery())
                .setFrom(0).setSize(100).setExplain(true)
                .execute()
                .actionGet();
        System.out.println(searchResponse.getHits().totalHits);
    }

    @Override
    public void createData() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("user", "hhh");
        json.put("postDate", "2018-06-28");
        json.put("message", "trying out Elasticsearch");

        IndexResponse response = esClient.getClient().prepareIndex(indexName,type,"4").setSource(json,XContentType.JSON).get();
        System.out.println(response.getResult());
    }

    @Override
    public void updateDocument() {

        try {
            //方法一：prepareUpdate
            XContentBuilder source = jsonBuilder().startObject().field("user","zhangliang").endObject();
            UpdateResponse updateResponse = esClient.getClient().prepareUpdate(indexName,type,"4").setDoc(source).get();
            System.out.println(updateResponse.getVersion());

            //方法二：UpdateRequest
            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.index(indexName).type(type).id("4").doc(jsonBuilder().startObject().field("user","zl").endObject());
            esClient.getClient().update(updateRequest).get();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDocument() {
        //删除指定记录
//        DeleteResponse response = esClient.getClient().prepareDelete(indexName,type,"b3e71163-62d5-487f-ac9a-32911a21772a").get();
//        System.out.println(response.getResult());
        System.out.println("==========");
        BulkByScrollResponse bulkByScrollResponse = DeleteByQueryAction.INSTANCE.newRequestBuilder(esClient.getClient()).filter(QueryBuilders.boolQuery()).source(indexName).get();
        System.out.println(bulkByScrollResponse.getDeleted());
    }

    @Override
    public void deleteByQueryAsync() {
        for (int i = 0; i < 1; i++) {
            DeleteByQueryAction.INSTANCE.newRequestBuilder(esClient.getClient())
                    .filter(QueryBuilders.boolQuery())
                    .source("testgoods")
                    //监听回调方法
                    .execute(new ActionListener<BulkByScrollResponse>() {
                        public void onResponse(BulkByScrollResponse response) {
                            long deleted = response.getDeleted();
                            System.out.println("删除的文档数量为= "+deleted);
                        }

                        public void onFailure(Exception e) {
                            System.out.println("Failure");
                        }
                    });
        }
    }
}
