package org.kaws.common.plugin.logging;

import cn.hutool.core.util.IdUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.kaws.common.annotation.Logging;
import org.kaws.common.domain.model.SysLog;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Bosco
 * @date 2022/4/8 12:23 上午
 */

@Component
@Aspect
public class LoggingAspect {


    @Pointcut("@annotation(org.kaws.common.annotation.Logging) || @within(org.kaws.common.annotation.Logging)")
    public void loggingPointCut() {
    }


    @Around("loggingPointCut()")
    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        SysLog sysLog = new SysLog();
        Object result;
        try {
            Logging loggingAnnotation = getLogging(joinPoint);
            sysLog.setId(IdUtil.getSnowflakeNextIdStr());
            sysLog.setTitle(loggingAnnotation.value());
            sysLog.setTitle(loggingAnnotation.title());
            sysLog.setDescription(loggingAnnotation.describe());
            sysLog.setSuccess(true);
            result = joinPoint.proceed();
        } catch (Exception exception) {
            sysLog.setSuccess(false);
            sysLog.setErrorMsg(exception.getMessage());
            throw exception;
        } finally {

        }
        return result;
    }


    private Logging getLogging(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<? extends Object> targetClass = point.getTarget().getClass();
        Logging targetLogging = targetClass.getAnnotation(Logging.class);
        if (targetLogging != null) {
            return targetLogging;
        } else {
            Method method = signature.getMethod();
            Logging logging = method.getAnnotation(Logging.class);
            return logging;
        }
    }

}
