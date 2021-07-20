package com.wenxia.miaosha.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-05-12
 */
@Setter
@Getter
public class ItemBase {

    private long id;

    /**
     * 商品编码
     */
    private String itemCode;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 商品价格
     */
    private long salePrice;
}
