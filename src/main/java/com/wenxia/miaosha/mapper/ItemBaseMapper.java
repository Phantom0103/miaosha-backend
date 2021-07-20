package com.wenxia.miaosha.mapper;

import com.wenxia.miaosha.entity.ItemBase;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-05-12
 */
public interface ItemBaseMapper {

    /**
     * 查询商品信息
     *
     * @param itemCode 商品编码
     * @return
     */
    @Select("select * from item_base where item_code = #{itemCode}")
    @Results({
            @Result(property = "itemCode", column = "item_code"),
            @Result(property = "itemName", column = "item_name"),
            @Result(property = "salePrice", column = "sale_price")
    })
    ItemBase getItemOne(String itemCode);
}
