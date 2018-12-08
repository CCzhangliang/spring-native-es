package com.zl.es.model;

import java.io.Serializable;

/**
 * <p> 类描述：
 * <p> 创建人: zhangliang
 * <p> 创建时间: 2018/12/7 2:50 PM
 * <p> 版权申明：Huobi All Rights Reserved
 */
public class Blog implements Serializable {
    private static final long serialVersionUID = 1L;
    private String user;
    private String postDate;
    private String message;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "user='" + user + '\'' +
                ", postDate='" + postDate + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
