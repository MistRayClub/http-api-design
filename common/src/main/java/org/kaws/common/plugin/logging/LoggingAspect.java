package org.kaws.common.plugin.logging;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.kaws.common.annotation.Logging;
import org.kaws.common.domain.dto.SysLogDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Bosco
 * @date 2022/4/8 12:23 上午
 */

@Slf4j
@Aspect
@Component
public class LoggingAspect {


    @Pointcut("@annotation(org.kaws.common.annotation.Logging) || @within(org.kaws.common.annotation.Logging)")
    public void loggingPointCut() {
    }


    @Around("loggingPointCut()")
    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Function<ServletRequest, String> getRequestBody = request -> {
            try {
                int len = request.getContentLength();
                ServletInputStream inputStream = request.getInputStream();
                byte[] buffer = new byte[len];
                inputStream.read(buffer, 0, len);
                return new String(buffer);
            } catch (IOException e) {
                log.error("parse request body error: {}", e.getMessage());
            }
            return null;
        };

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();

        SysLogDTO sysLogDTO = new SysLogDTO();
        Object result;
        try {
            Logging loggingAnnotation = getLogging(joinPoint);
            sysLogDTO.setId(IdUtil.getSnowflakeNextIdStr());
            sysLogDTO.setTitle(loggingAnnotation.value());
            sysLogDTO.setTitle(loggingAnnotation.title());
            sysLogDTO.setDescription(loggingAnnotation.describe());
            sysLogDTO.setSuccess(true);
            sysLogDTO.setOperateUrl(request.getRequestURI());
            sysLogDTO.setMethod(request.getMethod());
            sysLogDTO.setRequestParam(request.getQueryString());
            sysLogDTO.setRequestBody(getRequestBody.apply(request));
            result = joinPoint.proceed();
        } catch (Exception exception) {
            sysLogDTO.setSuccess(false);
            sysLogDTO.setErrorMsg(exception.getMessage());
            throw exception;
        } finally {
            sysLogDTO.setResponseBody(JSONUtil.toJsonStr(request));
            BlockingQueue<SysLogDTO> tempLogQueue = SpringUtil.getBean("logQueue");
            tempLogQueue.offer(sysLogDTO, 2, TimeUnit.SECONDS);
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
