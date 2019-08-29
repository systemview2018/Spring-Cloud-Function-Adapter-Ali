package com.siemens.mindsphere.cn.lib;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alex Wang
 * @version 1.0
 * @date           8/22/2019 15:15 PM
 * @modified by:   Alex Wang
 */
public class ApiResponseTest {
    ApiResponse apiResponse;

    @Before
    public void setUp() throws Exception {
        apiResponse = new ApiResponse(new HashMap(), false, 200, "aaa");
    }

    @Test
    public void getHeaders() throws Exception {
        assertThat(apiResponse.getHeaders().size()).isEqualTo(0);
    }

    @Test
    public void setHeaders() throws Exception {
        apiResponse.setHeaders(new HashMap());
        assertThat(apiResponse.getHeaders().size()).isEqualTo(0);
    }

    @Test
    public void getIsBase64Encoded() throws Exception {
        assertThat(apiResponse.getIsBase64Encoded()).isEqualTo(false);
    }

    @Test
    public void setIsBase64Encoded() throws Exception {
        apiResponse.setIsBase64Encoded(false);
        assertThat(apiResponse.getIsBase64Encoded()).isEqualTo(false);
    }

    @Test
    public void getStatusCode() throws Exception {
        assertThat(apiResponse.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void setStatusCode() throws Exception {
        apiResponse.setStatusCode(200);
        assertThat(apiResponse.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void getBody() throws Exception {
        assertThat(apiResponse.getBody()).isEqualTo("aaa");
    }

    @Test
    public void setBody() throws Exception {
        apiResponse.setBody("aaa");
        assertThat(apiResponse.getBody()).isEqualTo("aaa");
    }

}