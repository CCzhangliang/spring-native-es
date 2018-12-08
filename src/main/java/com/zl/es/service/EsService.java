package com.zl.es.service;

/**
 * <p> 类描述：
 * <p> 创建人: zhangliang
 * <p> 创建时间: 2018/12/7 4:10 PM
 * <p> 版权申明：Huobi All Rights Reserved
 */
public interface EsService {

    void queryDocument();

    void multiSearch();

    void createData();

    void updateDocument();

    void deleteDocument();

    void deleteByQueryAsync();
}
