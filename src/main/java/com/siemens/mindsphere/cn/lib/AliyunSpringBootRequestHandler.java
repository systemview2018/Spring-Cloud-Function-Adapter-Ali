package com.siemens.mindsphere.cn.lib;


import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.PojoRequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.AbstractSpringFunctionAdapterInitializer;
import org.springframework.cloud.function.context.catalog.FunctionInspector;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Alex Wang
 * @version 1.0
 * @date           8/22/2019 10:31 AM
 * @modified by:   Alex Wang
 */
@Slf4j
public class AliyunSpringBootRequestHandler<E, O> extends AbstractSpringFunctionAdapterInitializer<Context>
        implements PojoRequestHandler<E, O> {

    @Autowired
    protected FunctionInspector inspector;
    @Autowired
    protected ObjectMapper mapper;

    protected Context context;

    public AliyunSpringBootRequestHandler(Class<?> configurationClass) {
        super(configurationClass);
    }

    public AliyunSpringBootRequestHandler() {
        super();
    }

    @Override
    public O handleRequest(E event, Context context) {
        handleBeforeActions(event, context);
        this.context = context;
        initialize(context);
        handleActions(event, context);
        Object input = acceptsInput() ? convertEvent(event) : "";
        Publisher<?> output = apply(extract(input));
        handleAfterActions(event, context);
        return result(input, output);
    }

    protected void handleBeforeActions(E event, Context context) {}

    protected void handleActions(E event, Context context) {}

    protected void handleAfterActions(E event, Context context) {}

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T result(Object input, Publisher<?> output) {
        List<O> result = new ArrayList<>();
        for (Object value : Flux.from(output).toIterable()) {
            result.add(convertOutput(value));
        }
        if (isSingleValue(input) && result.size() == 1) {
            return (T) result.get(0);
        }
        return (T) result;
    }

    protected boolean acceptsInput() {
        return !this.getInspector().getInputType(function()).equals(Void.class);
    }

    protected boolean returnsOutput() {
        return !this.getInspector().getOutputType(function()).equals(Void.class);
    }

    private boolean isSingleValue(Object input) {
        return !(input instanceof Collection);
    }

    private Flux<?> extract(Object input) {
        if (input instanceof Collection) {
            return Flux.fromIterable((Iterable<?>) input);
        }
        return Flux.just(input);
    }

    protected Object convertEvent(E event) {
        return event;
    }

    @SuppressWarnings("unchecked")
    protected O convertOutput(Object output) {
        return (O) output;
    }

    protected boolean functionAcceptsMessage() {
        return this.inspector.isMessage(function());
    }

    protected boolean functionReturnsMessage(Object output) {
        return output instanceof Message;
    }
}
