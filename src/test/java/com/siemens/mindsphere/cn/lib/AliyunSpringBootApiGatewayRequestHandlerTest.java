package com.siemens.mindsphere.cn.lib;

import org.junit.After;
import org.junit.Test;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alex Wang
 * @version 1.0
 * @date           8/22/2019 14:56 PM
 * @modified by:   Alex Wang
 */
public class AliyunSpringBootApiGatewayRequestHandlerTest {

    private AliyunSpringBootApiGatewayRequestHandler handler;

    @After
    public void after() {
        System.clearProperty("function.name");
    }

    @Test
    public void supplierBean() {
        System.setProperty("function.name", "supplier");
        this.handler = new AliyunSpringBootApiGatewayRequestHandler(FunctionConfig.class);
        ApiRequest request = new ApiRequest();

        Object output = this.handler.handleRequest(request, null);
        assertThat(output).isInstanceOf(ApiResponse.class);
        assertThat(((ApiResponse) output).getStatusCode())
                .isEqualTo(200);
        assertThat(((ApiResponse) output).getBody())
                .isEqualTo("\"hello!\"");
    }

    @Test
    public void functionBean() {
        System.setProperty("function.name", "function");
        this.handler = new AliyunSpringBootApiGatewayRequestHandler(FunctionConfig.class);
        ApiRequest request = new ApiRequest();
        request.setBody("{\"value\":\"foo\"}");

        Object output = this.handler.handleRequest(request, null);
        assertThat(output).isInstanceOf(ApiResponse.class);
        assertThat(((ApiResponse) output).getStatusCode())
                .isEqualTo(200);
        assertThat(((ApiResponse) output).getBody())
                .isEqualTo("{\"value\":\"FOO\"}");

        ApiRequest bodyEncryptedRequest = new ApiRequest();
        bodyEncryptedRequest.setBody(
                Base64.getEncoder().encodeToString("{\"value\":\"foo\"}".getBytes()));
        bodyEncryptedRequest.setIsBase64Encoded(true);

        output = this.handler.handleRequest(bodyEncryptedRequest, null);
        assertThat(output).isInstanceOf(ApiResponse.class);
        assertThat(((ApiResponse) output).getStatusCode())
                .isEqualTo(200);
        assertThat(((ApiResponse) output).getBody())
                .isEqualTo("{\"value\":\"FOO\"}");
    }

    @Test
    public void consumerBean() {
        System.setProperty("function.name", "consumer");
        this.handler = new AliyunSpringBootApiGatewayRequestHandler(FunctionConfig.class);
        ApiRequest request = new ApiRequest();
        request.setBody("\"strVal\":\"test for consumer\"");

        Object output = this.handler.handleRequest(request, null);
        assertThat(output).isInstanceOf(ApiResponse.class);
        assertThat(((ApiResponse) output).getStatusCode())
                .isEqualTo(200);
    }

    @Test
    public void functionMessageBean() {
        this.handler = new AliyunSpringBootApiGatewayRequestHandler(
                FunctionMessageConfig.class);
        ApiRequest request = new ApiRequest();
        request.setBody("{\"value\":\"foo\"}");

        Object output = this.handler.handleRequest(request, null);
        assertThat(output).isInstanceOf(ApiResponse.class);
        assertThat(((ApiResponse) output).getStatusCode())
                .isEqualTo(200);
        assertThat(((ApiResponse) output).getHeaders().get("spring"))
                .isEqualTo("cloud");
        assertThat(((ApiResponse) output).getBody())
                .isEqualTo("{\"value\":\"FOO\"}");
    }


    @Test
    public void functionMessageBeanWithRequestParameters() {
        this.handler = new AliyunSpringBootApiGatewayRequestHandler(
                FunctionMessageEchoReqParametersConfig.class);
        ApiRequest request = new ApiRequest();
        request.setPathParameters(Collections.singletonMap("path", "pathValue"));
        request.setQueryParameters(Collections.singletonMap("query", "queryValue"));
        request.setHeaders(Collections.singletonMap("test-header", "headerValue"));
        request.setHttpMethod("GET");

        Object output = this.handler.handleRequest(request, null);
        assertThat(output).isInstanceOf(ApiResponse.class);
        assertThat(((ApiResponse) output).getStatusCode())
                .isEqualTo(200);
        assertThat(((ApiResponse) output).getHeaders().get("path"))
                .isEqualTo("pathValue");
        assertThat(((ApiResponse) output).getHeaders().get("query"))
                .isEqualTo("queryValue");
        assertThat(
                ((ApiResponse) output).getHeaders().get("test-header"))
                .isEqualTo("headerValue");
        assertThat(((ApiResponse) output).getHeaders().get("httpMethod"))
                .isEqualTo("GET");
        assertThat(((ApiResponse) output).getBody())
                .isEqualTo("{\"value\":\"body\"}");

    }

    @Test
    public void functionMessageBeanWithEmptyResponse() {
        this.handler = new AliyunSpringBootApiGatewayRequestHandler(
                FunctionMessageConsumerConfig.class);
        ApiRequest request = new ApiRequest();

        Object output = this.handler.handleRequest(request, null);
        assertThat(output).isInstanceOf(ApiResponse.class);
        assertThat(((ApiResponse) output).getStatusCode())
                .isEqualTo(200);
        assertThat(((ApiResponse) output).getBody()).isEmpty();
    }

    @Configuration
    @Import({ContextFunctionCatalogAutoConfiguration.class,
            JacksonAutoConfiguration.class})
    protected static class FunctionConfig {

        @Bean
        public Function<Foo, Bar> function() {
            return foo -> new Bar(foo.getValue().toUpperCase());
        }

        @Bean
        public Consumer<String> consumer() {
            return v -> System.out.println(v);
        }

        @Bean
        public Supplier<String> supplier() {
            return () -> "hello!";
        }

    }

    @Configuration
    @Import({ContextFunctionCatalogAutoConfiguration.class,
            JacksonAutoConfiguration.class})
    protected static class FunctionMessageConfig {

        @Bean
        public Function<Message<Foo>, Message<Bar>> function() {
            return (foo -> {
                Map<String, Object> headers = Collections.singletonMap("spring", "cloud");
                return new GenericMessage<>(
                        new Bar(foo.getPayload().getValue().toUpperCase()), headers);
            });
        }

    }

    @Configuration
    @Import({ContextFunctionCatalogAutoConfiguration.class,
            JacksonAutoConfiguration.class})
    protected static class FunctionMessageEchoReqParametersConfig {

        @Bean
        public Function<Message<Foo>, Message<Bar>> function() {
            return (message -> {
                Map<String, Object> headers = new HashMap<>();
                headers.put("path", message.getHeaders().get("path"));
                headers.put("query", message.getHeaders().get("query"));
                headers.put("test-header", message.getHeaders().get("test-header"));
                headers.put("httpMethod", message.getHeaders().get("httpMethod"));
                return new GenericMessage<>(new Bar("body"), headers);
            });
        }

    }

    @Configuration
    @Import({ContextFunctionCatalogAutoConfiguration.class,
            JacksonAutoConfiguration.class})
    protected static class FunctionMessageConsumerConfig {

        @Bean
        public Consumer<Message<Foo>> function() {
            return (foo -> {
            });
        }

    }

    protected static class Foo {

        private String value;

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    protected static class Bar {

        private String value;

        public Bar() {
        }

        public Bar(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

}