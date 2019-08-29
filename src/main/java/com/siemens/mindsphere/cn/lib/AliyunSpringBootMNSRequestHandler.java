package com.siemens.mindsphere.cn.lib;

import com.aliyun.fc.runtime.Context;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Alex Wang
 * @version 1.0
 * @date           8/22/2019 11:45 AM
 * @modified by:   Alex Wang
 */
@Slf4j
public class AliyunSpringBootMNSRequestHandler extends AliyunSpringBootRequestHandler<MnsRequest, String> {

    public AliyunSpringBootMNSRequestHandler(Class<?> configurationClass) {
        super(configurationClass);
    }

    public AliyunSpringBootMNSRequestHandler() {
        super();
    }

    @Override
    public String handleRequest(MnsRequest message, Context context) {
        super.handleRequest(message, context);
        return "";
    }

    @Override
    protected Object convertEvent(MnsRequest mnsRequest) {
        Object deserializedBody = (mnsRequest.Message != null && !mnsRequest.Message.isEmpty()) ? deserializeBody(mnsRequest.Message) : Optional.empty();
        return functionAcceptsMessage()
                ? new GenericMessage<>(deserializedBody, getHeaders(mnsRequest))
                : deserializedBody;
    }

    private MessageHeaders getHeaders(MnsRequest mnsRequest) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("publishTime", mnsRequest.PublishTime);
        headers.put("subscriptionName", mnsRequest.SubscriptionName);
        headers.put("topic", mnsRequest.TopicName);
        return new MessageHeaders(headers);
    }

    private Object deserializeBody(String json) {
        try {
            return mapper.readValue(json, getInputType());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot convert event", e);
        }
    }
}
