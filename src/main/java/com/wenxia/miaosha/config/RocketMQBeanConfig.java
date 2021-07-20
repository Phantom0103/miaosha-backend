package com.wenxia.miaosha.config;

import com.wenxia.miaosha.support.rocketmq.producer.OrderProducerBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-06-30
 */
@Configuration
public class RocketMQBeanConfig {

    @Value("${rocketmq.namesrv}")
    private String nameSrvAddress;

    @Value("${rocketmq.producer.retryTimesWhenSendFailed:3}")
    private int retryTimesWhenSendFailed;

    @Value("${rocketmq.order.group}")
    private String groupName;

    @Bean("producerConfig")
    public ProducerConfig registerProducerConfig() {
        ProducerConfig producerConfig = new ProducerConfig();
        producerConfig.setNameSrvAddress(nameSrvAddress);
        producerConfig.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);

        return producerConfig;
    }

    @Bean(value = "orderProducerBean", initMethod = "init", destroyMethod = "shutdown")
    public OrderProducerBean registerOrderProducerBean(@Qualifier("producerConfig") ProducerConfig producerConfig) {
        OrderProducerBean orderProducerBean = new OrderProducerBean();
        orderProducerBean.setConfig(producerConfig);
        orderProducerBean.setGroupName(groupName);

        return orderProducerBean;
    }
}
