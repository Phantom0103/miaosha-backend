package com.wenxia.miaosha.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-05-12
 */
@Getter
@Setter
public class SeckillActivityInfo implements Serializable {

    private long id;

    /**
     * 活动ID
     */
    private String activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 参与活动的商品编码
     */
    private String itemCode;

    /**
     * 活动价格
     */
    private long salePrice;

    /**
     * 活动数量
     */
    private int quantity;

    /**
     * 是否启用
     */
    private int enabled;
}
