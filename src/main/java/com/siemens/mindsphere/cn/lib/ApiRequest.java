package com.siemens.mindsphere.cn.lib;

import java.util.Map;

/**
 * @author Alex Wang
 * @version 1.0
 * @date           8/22/2019 10:27 AM
 * @modified by:   Alex Wang
 */
public class ApiRequest {
    private String path;
    private String httpMethod;
    private Map headers;
    private Map queryParameters;
    private Map pathParameters;
    private String body;
    private boolean isBase64Encoded;

    @Override
    public String toString() {
        return "Request{" +
                "path='" + path + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", headers=" + headers +
                ", queryParameters=" + queryParameters +
                ", pathParameters=" + pathParameters +
                ", body='" + body + '\'' +
                ", isBase64Encoded=" + isBase64Encoded +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Map getHeaders() {
        return headers;
    }

    public void setHeaders(Map headers) {
        this.headers = headers;
    }

    public Map getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(Map queryParameters) {
        this.queryParameters = queryParameters;
    }

    public Map getPathParameters() {
        return pathParameters;
    }

    public void setPathParameters(Map pathParameters) {
        this.pathParameters = pathParameters;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean getIsBase64Encoded() {
        return this.isBase64Encoded;
    }

    public void setIsBase64Encoded(boolean base64Encoded) {
        this.isBase64Encoded = base64Encoded;
    }
}