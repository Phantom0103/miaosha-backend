package com.wenxia.miaosha.service.impl;

import com.wenxia.miaosha.base.exception.InitActivityException;
import com.wenxia.miaosha.entity.ItemBase;
import com.wenxia.miaosha.entity.ItemStockBase;
import com.wenxia.miaosha.entity.SeckillActivityInfo;
import com.wenxia.miaosha.entity.SeckillOrder;
import com.wenxia.miaosha.mapper.ItemBaseMapper;
import com.wenxia.miaosha.mapper.SeckillActivityInfoMapper;
import com.wenxia.miaosha.service.SeckillService;
import com.wenxia.miaosha.support.annotation.OrderLock;
import com.wenxia.miaosha.support.rocketmq.SendResult;
import com.wenxia.miaosha.support.rocketmq.producer.OrderProducerBean;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.wenxia.miaosha.util.SystemConstants.*;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-05-13
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private SeckillActivityInfoMapper seckillActivityInfoMapper;

    @Autowired
    private ItemBaseMapper itemBaseMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Resource(name = "orderProducerBean")
    private OrderProducerBean orderProducerBean;

    @Value("${rocketmq.order.topic}")
    private String seckillOrderTopic;

    @Override
    public boolean isSaleOut(String itemCode) {
        RSet<String> itemSaleOut = redissonClient.getSet(ITEM_SALE_OUT);
        return itemSaleOut.contains(itemCode);
    }

    @Override
    @OrderLock("#activity.itemCode")
    public long stockReduce(String userId, int quantity, SeckillActivityInfo activity) {
        String itemCode = activity.getItemCode();
        String key = ITEM_STOCK_PREFIX + itemCode;
        RBucket<ItemStockBase> bucket = redissonClient.getBucket(key);
        ItemStockBase stock = bucket.get();

        int stockQuantity = stock.getQuantity();
        if (stockQuantity == 0) {
            // 售罄
            RSet<String> itemSaleOut = redissonClient.getSet(ITEM_SALE_OUT);
            itemSaleOut.add(itemCode);
            return -1;
        }

        if (quantity > stock.getQuantity()) {
            return -1;
        }

        stock.setQuantity(stockQuantity - quantity);
        bucket.set(stock);

        // 生成订单发送mq
        SeckillOrder order = createSeckillOrder(userId, activity, quantity);
        SendResult sr = orderProducerBean.send(order, seckillOrderTopic);

        if (sr.isSuccess()) {
            return order.getOrderNo();
        } else {
            return -1;
        }
    }

    @Override
    public SeckillActivityInfo getSeckillInfo(String activityId, String itemCode) {
        RLocalCachedMap<String, SeckillActivityInfo> activityCache = redissonClient.getLocalCachedMap(ACTIVITY_LOCAL_MAP, LocalCachedMapOptions.defaults());
        return activityCache.get(activityId + "-" + itemCode);
    }

    private long nextOrderId() {
        long now = System.currentTimeMillis();
        RAtomicLong rLong = redissonClient.getAtomicLong(NEXT_ORDER_NO);
        long v = rLong.getAndIncrement();
        if (v == 0) {
            LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            Date date = Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
            rLong.expireAt(date);
        }

        return now << 12 | v;
    }

    private SeckillOrder createSeckillOrder(String userId, SeckillActivityInfo activity, int quantity) {
        SeckillOrder order = new SeckillOrder();
        order.setOrderNo(nextOrderId());
        order.setActivityId(activity.getActivityId());
        order.setUserId(userId);
        order.setSalePrice(activity.getSalePrice());
        order.setItemCode(activity.getItemCode());
        order.setQuantity(quantity);

        return order;
    }

    @Override
    public void initSeckillActivity() {
        List<SeckillActivityInfo> activityInfos = seckillActivityInfoMapper.listAvailable();
        if (activityInfos == null || activityInfos.isEmpty()) {
            throw new InitActivityException("秒杀活动为空");
        }

        HashMap<String, SeckillActivityInfo> map = new HashMap<>();
        for (int i = 0; i < activityInfos.size(); i++) {
            SeckillActivityInfo activity = activityInfos.get(i);
            String itemCode = activity.getItemCode();
            ItemBase itemBase = itemBaseMapper.getItemOne(itemCode);
            if (itemBase == null) {
                throw new InitActivityException("商品不存在，item_code: " + itemCode);
            }

            ItemStockBase itemStockBase = new ItemStockBase();
            itemStockBase.setItemCode(itemCode);
            itemStockBase.setItemName(itemBase.getItemName());
            itemStockBase.setSalePrice(activity.getSalePrice());
            itemStockBase.setQuantity(activity.getQuantity());

            String key = ITEM_STOCK_PREFIX + itemCode;
            RBucket<ItemStockBase> bucket = redissonClient.getBucket(key);
            bucket.set(itemStockBase);

            map.put(activity.getActivityId() + "-" + itemCode, activity);
        }

        RMap<String, SeckillActivityInfo> activityMap = redissonClient.getMap(ACTIVITY_LOCAL_MAP);
        activityMap.putAll(map);
    }
}
