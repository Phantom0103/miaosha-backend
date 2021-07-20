package com.wenxia.miaosha.mapper;

import com.wenxia.miaosha.entity.SeckillActivityInfo;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-05-12
 */
public interface SeckillActivityInfoMapper {

    /**
     * 查询所有有效秒杀活动
     *
     * @return
     */
    @Select("select * from seckill_activity_info where enabled = 1")
    @Results({
            @Result(property = "activityId", column = "activity_id"),
            @Result(property = "activityName", column = "activity_name"),
            @Result(property = "itemCode", column = "item_code"),
            @Result(property = "salePrice", column = "sale_price")
    })
    List<SeckillActivityInfo> listAvailable();
}
