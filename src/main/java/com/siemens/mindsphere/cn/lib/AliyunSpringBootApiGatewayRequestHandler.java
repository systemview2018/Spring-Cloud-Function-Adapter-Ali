package com.siemens.mindsphere.cn.lib;


import com.aliyun.fc.runtime.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Alex Wang
 * @version 1.0
 * @date           8/22/2019 10:05 AM
 * @modified by:   Alex Wang
 */

@Slf4j
public class AliyunSpringBootApiGatewayRequestHandler extends AliyunSpringBootRequestHandler<ApiRequest, ApiResponse> {

    public AliyunSpringBootApiGatewayRequestHandler(Class<?> configurationClass) {
        super(configurationClass);
    }

    public AliyunSpringBootApiGatewayRequestHandler() {
        super();
    }

    @Override
    public ApiResponse handleRequest(ApiRequest event, Context context) {
        Object response = super.handleRequest(event, context);
        if (returnsOutput()) {
            return (ApiResponse) response;
        } else {
            return new ApiResponse(new HashMap(), false, 200, null);
        }
    }

    @Override
    protected Object convertEvent(ApiRequest request) {
        Object deserializedBody = (request.getBody() != null && !request.getBody().isEmpty()) ? deserializeBody(request) : Optional.empty();
        return functionAcceptsMessage()
                ? new GenericMessage<>(deserializedBody, getHeaders(request))
                : deserializedBody;
    }

    private Object deserializeBody(ApiRequest request) {
        try {
            this.mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            return this.mapper.readValue(
                    (request.getIsBase64Encoded())
                            ? new String(Base64.decodeBase64(request.getBody())) : request.getBody(),
                    getInputType());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot convert event", e);
        }
    }

    private MessageHeaders getHeaders(ApiRequest event) {
        Map<String, Object> headers = new HashMap<>();
        if (event.getHeaders() != null) {
            headers.putAll(event.getHeaders());
        }
        if (event.getQueryParameters() != null) {
            headers.putAll(event.getQueryParameters());
        }
        if (event.getPathParameters() != null) {
            headers.putAll(event.getPathParameters());
        }
        headers.put("httpMethod", event.getHttpMethod());
        headers.put("request", event);
        return new MessageHeaders(headers);
    }

    @Override
    protected ApiResponse convertOutput(Object output) {
        Map headers = new HashMap();
        boolean isBase64Encoded = false;
        int statusCode = 200;
        String returnBody = null;

        if (functionReturnsMessage(output)) {
            Message<?> message = (Message<?>) output;
            headers = toResponseHeaders(message.getHeaders());
            statusCode = (Integer) message.getHeaders().getOrDefault("statuscode", HttpStatus.OK.value());
            returnBody = serializeBody(message.getPayload());
        } else {
            returnBody = serializeBody(output);
        }

        return new ApiResponse(headers, isBase64Encoded, statusCode, returnBody);
    }


    private Map<String, String> toResponseHeaders(MessageHeaders messageHeaders) {
        Map<String, String> responseHeaders = new HashMap<>();
        messageHeaders.forEach((key, value) -> responseHeaders.put(key, value.toString()));
        return responseHeaders;
    }

    private String serializeBody(Object body) {
        try {
            return this.mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot convert output", e);
        }
    }
}
