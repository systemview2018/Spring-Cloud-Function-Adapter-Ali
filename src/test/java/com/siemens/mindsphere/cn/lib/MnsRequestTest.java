package com.siemens.mindsphere.cn.lib;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Alex Wang
 * @version 1.0
 * @date           8/28/2019 16:22 PM
 * @modified by:   Alex Wang
 */
public class MnsRequestTest {

    MnsRequest mnsRequest;

    @Before
    public void setUp() throws Exception {
        mnsRequest = new MnsRequest();
        mnsRequest.TopicOwner = "1394933273479206";
        mnsRequest.Message = "aaaaaaaaa";
        mnsRequest.Subscriber = "1394933273479206";
        mnsRequest.PublishTime = 1566963155535L;
        mnsRequest.SubscriptionName = "mns-test-480b7751";
        mnsRequest.MessageMD5 = "552E6A97297C53E592208CF97FBB3B60";
        mnsRequest.TopicName = "alex-test-function";
        mnsRequest.MessageId = "3FCF767DA8F47FC57FC88D85CA4F3148";
    }

    @Test
    public void test1() {
        mnsRequest.TopicOwner = "1394933273479206";
        mnsRequest.Message = "aaaaaaaaa";
        mnsRequest.Subscriber = "1394933273479206";
        mnsRequest.PublishTime = 1566963155535L;
        mnsRequest.SubscriptionName = "mns-test-480b7751";
        mnsRequest.MessageMD5 = "552E6A97297C53E592208CF97FBB3B60";
        mnsRequest.TopicName = "alex-test-function";
        mnsRequest.MessageId = "3FCF767DA8F47FC57FC88D85CA4F3148";
    }

    @Test
    public void toString1() {
        mnsRequest.toString();
    }
}