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
 * @date           8/28/2019 16:47 PM
 * @modified by:   Alex Wang
 */
public class AliyunSpringBootMnsRequestHandlerTest {

    private AliyunSpringBootMNSRequestHandler handler;

    @After
    public void after() {
        System.clearProperty("function.name");
    }

    @Test
    public void supplierBean() {
        System.setProperty("function.name", "supplier");
        this.handler = new AliyunSpringBootMNSRequestHandler(AliyunSpringBootMnsRequestHandlerTest.FunctionConfig.class);
        MnsRequest request = new MnsRequest();

        Object output = this.handler.handleRequest(request, null);
        assertThat(output).isInstanceOf(String.class);
    }


    @Test
    public void functionMessageBeanWithEmptyResponse() {
        this.handler = new AliyunSpringBootMNSRequestHandler(
                AliyunSpringBootMnsRequestHandlerTest.FunctionMessageConsumerConfig.class);
        MnsRequest request = new MnsRequest();

        Object output = this.handler.handleRequest(request, null);
        assertThat(output).isInstanceOf(String.class);
    }

    @Configuration
    @Import({ContextFunctionCatalogAutoConfiguration.class,
            JacksonAutoConfiguration.class})
    protected static class FunctionConfig {

        @Bean
        public Function<AliyunSpringBootMnsRequestHandlerTest.Foo, AliyunSpringBootMnsRequestHandlerTest.Bar> function() {
            return foo -> new AliyunSpringBootMnsRequestHandlerTest.Bar(foo.getValue().toUpperCase());
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
        public Function<Message<AliyunSpringBootMnsRequestHandlerTest.Foo>, Message<AliyunSpringBootMnsRequestHandlerTest.Bar>> function() {
            return (foo -> {
                Map<String, Object> headers = Collections.singletonMap("spring", "cloud");
                return new GenericMessage<>(
                        new AliyunSpringBootMnsRequestHandlerTest.Bar(foo.getPayload().getValue().toUpperCase()), headers);
            });
        }

    }

    @Configuration
    @Import({ContextFunctionCatalogAutoConfiguration.class,
            JacksonAutoConfiguration.class})
    protected static class FunctionMessageEchoReqParametersConfig {

        @Bean
        public Function<Message<AliyunSpringBootMnsRequestHandlerTest.Foo>, Message<AliyunSpringBootMnsRequestHandlerTest.Bar>> function() {
            return (message -> {
                Map<String, Object> headers = new HashMap<>();
                headers.put("path", message.getHeaders().get("path"));
                headers.put("query", message.getHeaders().get("query"));
                headers.put("test-header", message.getHeaders().get("test-header"));
                headers.put("httpMethod", message.getHeaders().get("httpMethod"));
                return new GenericMessage<>(new AliyunSpringBootMnsRequestHandlerTest.Bar("body"), headers);
            });
        }

    }

    @Configuration
    @Import({ContextFunctionCatalogAutoConfiguration.class,
            JacksonAutoConfiguration.class})
    protected static class FunctionMessageConsumerConfig {

        @Bean
        public Consumer<Message<AliyunSpringBootMnsRequestHandlerTest.Foo>> function() {
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