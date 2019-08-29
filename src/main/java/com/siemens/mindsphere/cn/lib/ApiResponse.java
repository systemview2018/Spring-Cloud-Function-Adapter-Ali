package com.siemens.mindsphere.cn.lib;

import java.util.Map;

/**
 * @author Alex Wang
 * @version 1.0
 * @date           8/22/2019 10:27 AM
 * @modified by:   Alex Wang
 */
public class ApiResponse {
    private Map headers;
    private boolean isBase64Encoded;
    private int statusCode;
    private String body;

    public ApiResponse(Map headers, boolean isBase64Encoded, int statusCode, String body) {
        this.headers = headers;
        this.isBase64Encoded = isBase64Encoded;
        this.statusCode = statusCode;
        this.body = body;
    }

    public Map getHeaders() {
        return headers;
    }

    public void setHeaders(Map headers) {
        this.headers = headers;
    }

    public boolean getIsBase64Encoded() {
        return isBase64Encoded;
    }

    public void setIsBase64Encoded(boolean base64Encoded) {
        this.isBase64Encoded = base64Encoded;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
