package com.wenxia.miaosha.support.aspect;

import com.wenxia.miaosha.base.exception.LockFailException;
import com.wenxia.miaosha.support.annotation.OrderLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-05-13
 */
@Component
@Aspect
public class OrderLockAspect {

    @Autowired
    private RedissonClient redissonClient;

    @Around("@annotation(orderLock)")
    public Object invoke(ProceedingJoinPoint pjp, OrderLock orderLock) throws Throwable {
        Object lockObject = getLockObject(pjp, orderLock.value());
        if (lockObject == null) {
            throw new LockFailException("无法获取加锁对象");
        }

        RLock lock = redissonClient.getLock(lockObject.toString());
        boolean isLock = false;
        try {
            // 尝试在1000ms内获取锁，5000ms锁失效时间
            isLock = lock.tryLock(1000, 5000, TimeUnit.MILLISECONDS);
            if (isLock) {
                return pjp.proceed();
            }

            throw new LockFailException("对象\"" + lockObject + "\"加锁失败");
        } finally {
            if (isLock) {
                lock.unlock();
            }
        }
    }

    /**
     * 获取加锁的对象
     *
     * @param pjp
     * @param el
     * @return
     */
    private Object getLockObject(ProceedingJoinPoint pjp, String el) {
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            String[] parameterNames = signature.getParameterNames();
            Object[] args = pjp.getArgs();
            if (args == null || parameterNames == null) {
                return null;
            }

            ExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(el);
            EvaluationContext context = new StandardEvaluationContext();

            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }

            return expression.getValue(context);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
