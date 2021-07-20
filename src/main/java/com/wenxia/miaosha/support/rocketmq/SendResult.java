package com.wenxia.miaosha.support.rocketmq;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-06-29
 */
public class SendResult {

    private boolean success;
    private String messageId;
    private String topic;
    private String errorMessage;

    public SendResult() {

    }

    public SendResult(String topic) {
        this.topic = topic;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
