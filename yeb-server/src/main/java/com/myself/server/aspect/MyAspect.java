package com.myself.server.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 切面类
 *
 * @author Wei
 * @Date 2021/8/12
 */
@Component
@Aspect
@Slf4j
public class MyAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyAspect.class);

    @Pointcut(value = "execution(public * com.myself.server.controller.*.*(..))")
    public void myPointCut() {
    }

    @Around(value = "myPointCut()")
    public void myAround(ProceedingJoinPoint joinPoint) {
        LOGGER.info(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "开始执行。");
        try {
            joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        LOGGER.info(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "执行结束。");
    }

}
