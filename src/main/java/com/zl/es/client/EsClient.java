package com.zl.es.client;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author zhangliang
 */
@Service("esClient")
public class EsClient {

    private Logger log = LoggerFactory.getLogger(getClass());
    private TransportClient client = null;

    //索引库名
    @Value("${elasticsearch.indexName}")
    private String indexName;
    //类型名称
    @Value("${elasticsearch.type}")
    private String type;
    @Value("${elasticsearch.cluster-nodes:127.0.0.1:9300}")
    private String cluster;

    @PostConstruct
    private void initClient(){
        try {
            Settings settings = Settings.builder()
                    //集群名称
                    .put("cluster.name","elasticsearch")
                    //自动嗅探
                    .put("client.transport.sniff", true)
                    .build();
            client = new PreBuiltTransportClient(settings);
            String[] clusterNodes = cluster.split(",");
            for (int i = 0 ; i < clusterNodes.length ; i++){
                client.addTransportAddress(new TransportAddress(InetAddress.getByName(clusterNodes[i].split(":")[0]),Integer.valueOf(clusterNodes[i].split(":")[1])));
            }

            //启动程序不报错就代表已经成功和es建立连接

            //查看集群信息
            List<DiscoveryNode> nodes = client.listedNodes();

        } catch (UnknownHostException e) {
            log.error("初始化es失败：{}",e);
        }

    }

    public synchronized TransportClient getClient(){
        if (client == null){
            initClient();
        }
        return client;
    }

//    /**
//     * 手动方式
//     * @throws UnknownHostException
//     */
//    @Test
//    public void JsonDocument() throws UnknownHostException {
//        String json = "{" +
//                "\"user\":\"deepredapple\"," +
//                "\"postDate\":\"2018-01-30\"," +
//                "\"message\":\"trying out Elasticsearch\"" +
//                "}";
//        IndexResponse indexResponse = client.prepareIndex(indexName, type).setSource(json).get();
//        System.out.println(indexResponse.getResult());
//    }
//
//    /**
//     * Map方式
//     */
//    @Test
//    public void MapDocument() {
//        Map<String, Object> json = new HashMap<String, Object>();
//        json.put("user", "hhh");
//        json.put("postDate", "2018-06-28");
//        json.put("message", "trying out Elasticsearch");
//        IndexResponse response = client.prepareIndex(indexName, type).setSource(json).get();
//        System.out.println(response.getResult());
//    }
//
//    /**
//     * 使用JACKSON序列化
//     */
//    @Test
//    public void JACKSONDocument() throws JsonProcessingException {
//        Blog blog = new Blog();
//        blog.setUser("123");
//        blog.setPostDate("2018-06-29");
//        blog.setMessage("try out ElasticSearch");
//
//        ObjectMapper mapper = new ObjectMapper();
//        byte[] bytes = mapper.writeValueAsBytes(blog);
//        IndexResponse response = client.prepareIndex(indexName, type).setSource(bytes).get();
//        System.out.println(response.getResult());
//    }
//
//    /**
//     * 使用XContentBuilder帮助类方式
//     */
//    @Test
//    public void XContentBuilderDocument() throws IOException {
//        XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
//                .field("user", "xcontentdocument")
//                .field("postDate", "2018-06-30")
//                .field("message", "this is ElasticSearch").endObject();
//        IndexResponse response = client.prepareIndex(indexName, type).setSource(builder).get();
//        System.out.println(response.getResult());
//    }
//
}
