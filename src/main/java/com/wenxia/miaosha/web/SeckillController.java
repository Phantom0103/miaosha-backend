package com.wenxia.miaosha.web;

import com.wenxia.miaosha.base.OneResponse;
import com.wenxia.miaosha.entity.SeckillActivityInfo;
import com.wenxia.miaosha.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-05-11
 */
@RestController
@RequestMapping("/seckill")
public class SeckillController {

    private static final Logger logger = LoggerFactory.getLogger(SeckillController.class);

    @Autowired
    private SeckillService seckillService;

    @PostMapping("/order")
    public OneResponse order(String activityId, String itemCode, String userId, int quantity) {
        if (quantity < 1 || quantity > 2) {
            return new OneResponse<>(-1, "抢购数量超过限制范围");
        }

        SeckillActivityInfo activity = seckillService.getSeckillInfo(activityId, itemCode);
        if (activity == null) {
            return new OneResponse(-1, "秒杀活动不存在");
        }

        boolean isSaleOut = seckillService.isSaleOut(itemCode);
        if (isSaleOut) {
            return new OneResponse(-1, "商品已经卖完了");
        }

        try {
            long orderNo = seckillService.stockReduce(userId, quantity, activity);
            if (orderNo > 0) {
                return new OneResponse<>(orderNo);
            }
        } catch (Exception e) {
            return new OneResponse(-1, e.getMessage());
        }

        return new OneResponse(-1, "很遗憾，抢购失败");
    }

    @PostMapping("/init")
    public OneResponse initSeckillActivity() {
        try {
            seckillService.initSeckillActivity();
            return new OneResponse();
        } catch (Exception e) {
            logger.error("初始化秒杀活动失败", e);
            return new OneResponse(-1, "初始化秒杀活动失败，" + e.getMessage());
        }
    }
}
