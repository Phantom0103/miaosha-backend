package com.wenxia.miaosha.support.rocketmq.impl;

import com.wenxia.miaosha.base.exception.CustomMQClientException;
import com.wenxia.miaosha.config.ProducerConfig;
import com.wenxia.miaosha.support.rocketmq.Producer;
import com.wenxia.miaosha.support.rocketmq.SendResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.RPCHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-06-29
 */
public class OrderProducerImpl implements Producer {

    private static final Logger logger = LoggerFactory.getLogger(OrderProducerImpl.class);

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private ProducerConfig producerConfigurer;
    private String group;

    private DefaultMQProducer producer;

    public OrderProducerImpl(ProducerConfig configurer, String groupName) {
        this(configurer, groupName, null);
    }

    public OrderProducerImpl(ProducerConfig configurer, String groupName, RPCHook hook) {
        if (configurer == null) {
            throw new CustomMQClientException("生产者OrderProducer配置为空");
        }

        this.producerConfigurer = configurer;
        this.group = StringUtils.isBlank(groupName) ? "SECKILL-GROUP" : groupName;

        producer = new DefaultMQProducer(group, hook);
        producer.setNamesrvAddr(producerConfigurer.getNameSrvAddress());
        producer.setRetryTimesWhenSendFailed(producerConfigurer.getRetryTimesWhenSendFailed());
        producer.setRetryTimesWhenSendAsyncFailed(producerConfigurer.getRetryTimesWhenSendFailed());
    }

    @Override
    public void start() {
        if (this.isRunning.compareAndSet(false, true)) {
            try {
                producer.start();
                logger.info("生产者OrderProducer启动成功");
            } catch (Exception e) {
                logger.error("生产者OrderProducer启动失败", e);
                this.isRunning.set(false);
                throw new CustomMQClientException("生产者启动失败", e);
            }
        }
    }

    @Override
    public void shutdown() {
        if (this.isRunning.compareAndSet(true, false)) {
            if (producer != null) {
                producer.shutdown();
            }
        }
    }

    @Override
    public SendResult send(Message message) {
        SendResult sendResult = new SendResult(message.getTopic());
        try {
            org.apache.rocketmq.client.producer.SendResult result = producer.send(message);
            SendStatus sendStatus = result.getSendStatus();
            if (SendStatus.SEND_OK == sendStatus) {
                sendResult.setSuccess(true);
                sendResult.setMessageId(result.getMsgId());
            } else {
                sendResult.setErrorMessage(sendStatus.name());
                logger.error("OrderProducer发送消息失败。{}", result);
            }
        } catch (Exception e) {
            logger.error("OrderProducer发送消息失败", e);
            sendResult.setErrorMessage(e.getMessage());
        }

        return sendResult;
    }
}
