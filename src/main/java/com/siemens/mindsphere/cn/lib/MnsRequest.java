package com.siemens.mindsphere.cn.lib;

/**
 * @author Alex Wang
 * @version 1.0
 * @date           8/22/2019 11:41 AM
 * @modified by:   Alex Wang
 */
public class MnsRequest {

    public String TopicOwner;
    public String Message;
    public String Subscriber;
    public long PublishTime;
    public String SubscriptionName;
    public String MessageMD5;
    public String TopicName;
    public String MessageId;

    @Override
    public String toString() {
        return "MnsRequest{" +
                "TopicOwner='" + TopicOwner + '\'' +
                ", Message='" + Message + '\'' +
                ", Subscriber='" + Subscriber + '\'' +
                ", PublishTime=" + PublishTime +
                ", SubscriptionName='" + SubscriptionName + '\'' +
                ", MessageMD5='" + MessageMD5 + '\'' +
                ", TopicName='" + TopicName + '\'' +
                ", MessageId='" + MessageId + '\'' +
                '}';
    }
}
