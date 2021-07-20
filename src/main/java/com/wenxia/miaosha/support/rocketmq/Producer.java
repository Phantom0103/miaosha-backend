package com.wenxia.miaosha.support.rocketmq;

import org.apache.rocketmq.common.message.Message;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-06-29
 */
public interface Producer {

    void start();

    void shutdown();

    SendResult send(Message message);
}
