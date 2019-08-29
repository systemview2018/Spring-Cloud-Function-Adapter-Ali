package com.siemens.mindsphere.cn.lib;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alex Wang
 * @version 1.0
 * @date           8/22/2019 14:56 PM
 * @modified by:   Alex Wang
 */
public class AliyunSpringBootStreamHandlerTest {
    private AliyunSpringBootStreamHandler handler;

    @Before
    public void before() {
        System.clearProperty("function.name");
    }

    @Test
    public void functionBeanWithJacksonConfig() throws Exception {
        this.handler = new AliyunSpringBootStreamHandler(FunctionConfigWithJackson.class);
        this.handler.initialize(null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        this.handler.handleRequest(
                new ByteArrayInputStream("{\"value\":\"foo\"}".getBytes()), output, null);
        assertThat(output.toString()).isEqualTo("{\"value\":\"FOO\"}");
    }

    @Test
    public void functionBeanWithoutJacksonConfig() throws Exception {
        this.handler = new AliyunSpringBootStreamHandler(FunctionConfigWithoutJackson.class);
        this.handler.initialize(null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        this.handler.handleRequest(
                new ByteArrayInputStream("{\"value\":\"foo\"}".getBytes()), output, null);
        assertThat(output.toString()).isEqualTo("{\"value\":\"FOO\"}");
    }

    @Test
    public void functionNonFluxBeanNoCatalog() throws Exception {
        this.handler = new AliyunSpringBootStreamHandler(NoCatalogNonFluxFunctionConfig.class);
        this.handler.initialize(null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        this.handler.handleRequest(
                new ByteArrayInputStream("{\"value\":\"foo\"}".getBytes()), output, null);
        assertThat(output.toString()).isEqualTo("{\"value\":\"FOO\"}");
    }

    //    @Test
    //    public void functionFluxBeanNoCatalog() throws Exception {
    //        this.handler = new AliyunSpringBootStreamHandler(NoCatalogFluxFunctionConfig.class);
    //        this.handler.initialize(null);
    //        ByteArrayOutputStream output = new ByteArrayOutputStream();
    //        this.handler.handleRequest(
    //                new ByteArrayInputStream("{\"value\":\"foo\"}".getBytes()), output, null);
    //        assertThat(output.toString()).isEqualTo("{\"value\":\"FOO\"}");
    //    }

    @Test
    public void typelessFunctionConfig() throws Exception {
        this.handler = new AliyunSpringBootStreamHandler(TypelessFunctionConfig.class);
        this.handler.initialize(null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        this.handler.handleRequest(
                new ByteArrayInputStream("{\"value\":\"foo\"}".getBytes()), output, null);
        assertThat(output.toString()).isEqualTo("{\"value\":\"foo\"}");
    }

    @Test
    public void inputStreamFunctionConfig() throws Exception {
        this.handler = new AliyunSpringBootStreamHandler(InputStreamFunctionConfig.class);
        this.handler.initialize(null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        this.handler.handleRequest(
                new ByteArrayInputStream("{\"value\":\"foo\"}".getBytes()), output, null);
        assertThat(output.toString()).isEqualTo("{\"value\":\"FOO\"}");
    }

    @Configuration
    protected static class NoCatalogNonFluxFunctionConfig {

        @Bean
        public Function<Foo, Bar> function() {
            return foo -> new Bar(foo.getValue().toUpperCase());
        }

    }

    @Configuration
    protected static class NoCatalogFluxFunctionConfig {

        @Bean
        public Function<Flux<Foo>, Flux<Bar>> function() {
            return flux -> flux.map(foo -> new Bar(foo.getValue().toUpperCase()));
        }

    }

    @Configuration
    @Import({ContextFunctionCatalogAutoConfiguration.class,
            JacksonAutoConfiguration.class})
    protected static class FunctionConfigWithJackson {

        @Bean
        public Function<Foo, Bar> function() {
            return foo -> new Bar(foo.getValue().toUpperCase());
        }

    }

    @Configuration
    @Import({ContextFunctionCatalogAutoConfiguration.class})
    protected static class FunctionConfigWithoutJackson {

        @Bean
        public Function<Foo, Bar> function() {
            return foo -> new Bar(foo.getValue().toUpperCase());
        }

    }

    @Configuration
    @Import({ContextFunctionCatalogAutoConfiguration.class,
            JacksonAutoConfiguration.class})
    protected static class TypelessFunctionConfig {

        @Bean
        public Function<?, ?> function() {
            return value -> {
                Assert.isTrue(value instanceof Map, "Expected value should be Map");
                return value;
            };
        }

    }

    @Configuration
    @Import({ContextFunctionCatalogAutoConfiguration.class,
            JacksonAutoConfiguration.class})
    protected static class InputStreamFunctionConfig {

        @Autowired
        private ObjectMapper mapper;

        @Bean
        public Function<InputStream, ?> function() {
            return value -> {
                try {
                    Foo foo = this.mapper.readValue((InputStream) value, Foo.class);
                    return new Bar(foo.getValue().toUpperCase());
                } catch (Exception e) {
                    throw new IllegalStateException("Failed test", e);
                }
            };
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