package com.wenxia.miaosha.service;

import com.wenxia.miaosha.entity.SeckillActivityInfo;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-05-13
 */
public interface SeckillService {

    /**
     * 秒杀扣件库存
     *
     * @param userId
     * @param quantity
     * @param activity
     * @return orderNo
     */
    long stockReduce(String userId, int quantity, SeckillActivityInfo activity);

    /**
     * 是否售罄
     *
     * @param itemCode
     * @return
     */
    boolean isSaleOut(String itemCode);

    /**
     * 获取秒杀活动信息
     *
     * @param activityId
     * @param itemCode
     * @return
     */
    SeckillActivityInfo getSeckillInfo(String activityId, String itemCode);

    /**
     * 初始化秒杀活动
     */
    void initSeckillActivity();
}
