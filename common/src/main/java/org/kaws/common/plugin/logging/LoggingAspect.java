package org.kaws.common.plugin.logging;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

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
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();

        SysLogDTO sysLogDTO = new SysLogDTO();
        Object result = null;
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
            Object[] args = joinPoint.getArgs();
            List<Object> filterList = new ArrayList<>();
            for (Object arg : args) {
                if (arg instanceof ServletRequest || arg instanceof ServletResponse) continue;
                filterList.add(arg);
            }
            sysLogDTO.setRequestBody(JSONUtil.toJsonStr(filterList));
            result = joinPoint.proceed();
        } catch (Exception exception) {
            sysLogDTO.setSuccess(false);
            sysLogDTO.setErrorMsg(exception.getMessage());
            throw exception;
        } finally {
            sysLogDTO.setResponseBody(SpringUtil.getBean(ObjectMapper.class).writeValueAsString(result));
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
