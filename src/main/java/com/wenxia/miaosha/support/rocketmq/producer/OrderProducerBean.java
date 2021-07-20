package com.wenxia.miaosha.support.rocketmq.producer;

import com.wenxia.miaosha.config.ProducerConfig;
import com.wenxia.miaosha.entity.SeckillOrder;
import com.wenxia.miaosha.support.rocketmq.SendResult;
import com.wenxia.miaosha.support.rocketmq.impl.OrderProducerImpl;
import org.apache.rocketmq.common.message.Message;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-06-29
 */
public class OrderProducerBean {

    private OrderProducerImpl producer;

    private ProducerConfig config;
    private String groupName;

    public void init() {
        producer = new OrderProducerImpl(config, groupName);
        producer.start();
    }

    public void shutdown() {
        if (producer != null) {
            producer.shutdown();
        }
    }

    public SendResult send(SeckillOrder order, String topic) {
        Message message = new Message();
        message.setTopic(topic);
        message.setTags("seckill-order");
        message.setKeys(String.valueOf(order.getOrderNo()));
        message.setBody(order.toString().getBytes());

        return producer.send(message);
    }

    public ProducerConfig getConfig() {
        return config;
    }

    public void setConfig(ProducerConfig config) {
        this.config = config;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
