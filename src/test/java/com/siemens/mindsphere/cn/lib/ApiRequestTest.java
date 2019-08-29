package com.siemens.mindsphere.cn.lib;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alex Wang
 * @version 1.0
 * @date           8/22/2019 15:12 PM
 * @modified by:   Alex Wang
 */
public class ApiRequestTest {

    ApiRequest apiRequest;

    @Before
    public void setUp() throws Exception {
        apiRequest = new ApiRequest();
        apiRequest.setPath("abc");
        apiRequest.setQueryParameters(new HashMap());
        apiRequest.setBody("abc");
        apiRequest.setHeaders(new HashMap());
        apiRequest.setHttpMethod("abc");
        apiRequest.setIsBase64Encoded(false);
        apiRequest.setPathParameters(new HashMap());
    }

    @Test
    public void getPath() throws Exception {
        assertThat(apiRequest.getPath()).isEqualTo("abc");
    }

    @Test
    public void setPath() throws Exception {
        apiRequest.setPath("abc");
        assertThat(apiRequest.getPath()).isEqualTo("abc");
    }

    @Test
    public void getHttpMethod() throws Exception {
        assertThat(apiRequest.getHttpMethod()).isEqualTo("abc");
    }

    @Test
    public void setHttpMethod() throws Exception {
        apiRequest.setHttpMethod("abc");
        assertThat(apiRequest.getHttpMethod()).isEqualTo("abc");
    }

    @Test
    public void getHeaders() throws Exception {
        assertThat(apiRequest.getHeaders().size()).isEqualTo(0);
    }

    @Test
    public void setHeaders() throws Exception {
        apiRequest.setHeaders(new HashMap());
        assertThat(apiRequest.getHeaders().size()).isEqualTo(0);
    }

    @Test
    public void getQueryParameters() throws Exception {
        assertThat(apiRequest.getQueryParameters().size()).isEqualTo(0);
    }

    @Test
    public void setQueryParameters() throws Exception {
        apiRequest.setQueryParameters(new HashMap());
        assertThat(apiRequest.getQueryParameters().size()).isEqualTo(0);
    }

    @Test
    public void getPathParameters() throws Exception {
        assertThat(apiRequest.getPathParameters().size()).isEqualTo(0);
    }

    @Test
    public void setPathParameters() throws Exception {
        apiRequest.setPathParameters(new HashMap());
        assertThat(apiRequest.getPathParameters().size()).isEqualTo(0);
    }

    @Test
    public void getBody() throws Exception {
        assertThat(apiRequest.getBody()).isEqualTo("abc");
    }

    @Test
    public void setBody() throws Exception {
        apiRequest.setBody("abc");
        assertThat(apiRequest.getBody()).isEqualTo("abc");
    }

    @Test
    public void getIsBase64Encoded() throws Exception {
        assertThat(apiRequest.getIsBase64Encoded()).isEqualTo(false);
    }

    @Test
    public void setIsBase64Encoded() throws Exception {
        apiRequest.setIsBase64Encoded(false);
        assertThat(apiRequest.getIsBase64Encoded()).isEqualTo(false);
    }

}