package com.wenxia.miaosha.config;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-06-29
 */
public class ProducerConfig {

    private String nameSrvAddress;

    private int retryTimesWhenSendFailed = 3;

    public String getNameSrvAddress() {
        return nameSrvAddress;
    }

    public void setNameSrvAddress(String nameSrvAddress) {
        this.nameSrvAddress = nameSrvAddress;
    }

    public int getRetryTimesWhenSendFailed() {
        return retryTimesWhenSendFailed;
    }

    public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
    }
}
