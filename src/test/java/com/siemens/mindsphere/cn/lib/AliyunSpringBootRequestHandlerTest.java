package com.siemens.mindsphere.cn.lib;

import org.junit.Test;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alex Wang
 * @version 1.0
 * @date           8/22/2019 14:56 PM
 * @modified by:   Alex Wang
 */
public class AliyunSpringBootRequestHandlerTest {

    private AliyunSpringBootRequestHandler<Foo, Bar> handler;

    @Test
    public void functionBean() throws Exception {
        this.handler = new AliyunSpringBootRequestHandler<Foo, Bar>(FunctionConfig.class);
        Object output = this.handler.handleRequest(new Foo("foo"), null);
        assertThat(output).isInstanceOf(Bar.class);
    }

    @Configuration
    @Import({ContextFunctionCatalogAutoConfiguration.class,
            JacksonAutoConfiguration.class})
    protected static class FunctionConfig {

        @Bean
        public Function<Foo, Bar> function() {
            return foo -> new Bar(foo.getValue().toUpperCase());
        }

    }

    protected static class Foo {

        private String value;

        public Foo(String value) {
            this.value = value;
        }

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