package com.siemens.mindsphere.cn.lib;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.StreamRequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.AbstractSpringFunctionAdapterInitializer;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

/**
 * @author Alex Wang
 * @version 1.0
 * @date           8/22/2019 11:58 AM
 * @modified by:   Alex Wang
 */
@Slf4j
public class AliyunSpringBootStreamHandler extends AbstractSpringFunctionAdapterInitializer<Context>
        implements StreamRequestHandler {

    @Autowired(required = false)
    protected ObjectMapper mapper;

    protected Context context;

    public AliyunSpringBootStreamHandler() {
        super();
    }

    public AliyunSpringBootStreamHandler(Class<?> configurationClass) {
        super(configurationClass);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        this.context = context;
        initialize(context);
        Object value = convertStream(inputStream);
        Publisher<?> flux = apply(extract(value));
        this.mapper.writeValue(outputStream, result(value, flux));
    }

    @Override
    protected void initialize(Context context) {
        super.initialize(context);
        if (this.mapper == null) {
            this.mapper = new ObjectMapper();
        }
    }

    private Flux<?> extract(Object input) {
        if (input instanceof Collection) {
            return Flux.fromIterable((Iterable<?>) input);
        }
        return Flux.just(input);
    }

    /*
     * Will convert to POJOP or generic map unless user
     * explicitly requests InputStream (e.g., Function<InputStream, ?>).
     */
    private Object convertStream(InputStream input) {
        Object convertedResult = input;
        try {
            Class<?> inputType = getInputType();
            if (!InputStream.class.isAssignableFrom(inputType)) {
                convertedResult = this.mapper.readValue(input, inputType);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot convert event stream", e);
        }
        return convertedResult;
    }


}
