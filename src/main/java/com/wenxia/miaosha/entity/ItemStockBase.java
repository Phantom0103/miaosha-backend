package com.wenxia.miaosha.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-05-28
 */
@Getter
@Setter
public class ItemStockBase implements Serializable {

    /**
     * 商品编码
     */
    private String itemCode;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 库存
     */
    private int quantity;

    private long salePrice;
}
