package com.wenxia.miaosha.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-06-30
 */
@Setter
@Getter
public class SeckillOrder {

    private long orderNo;
    private String userId;
    private String itemCode;
    private int quantity;
    private long salePrice;
    private String activityId;

    @Override
    public String toString() {
        return "{\"order_no\":" + orderNo + "," +
                "\"user_id\":\"" + userId + "\"," +
                "\"item_code\":\"" + itemCode + "\"," +
                "\"quantity\":" + quantity + "," +
                "\"sale_price\":" + salePrice + "," +
                "\"activity_id\":\"" + activityId + "\"}";
    }
}
