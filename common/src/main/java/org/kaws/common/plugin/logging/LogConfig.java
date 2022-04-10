package org.kaws.common.plugin.logging;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReferenceUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.kaws.common.constant.BaseConstant;
import org.kaws.common.domain.dto.SysLogDTO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Bosco
 * @date 2022/4/9 7:51 下午
 */

@Slf4j
@Configuration
public class LogConfig implements InitializingBean {

    private static final Integer BLOCKING_QUEUE_SIZE = 1000000;

    @Override
    public void afterPropertiesSet() throws Exception {

        BlockingQueue<SysLogDTO> logQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_SIZE);
        SpringUtil.registerBean("logQueue", logQueue);

        ThreadUtil.schedule(BaseConstant.LOG_EXECUTOR, () -> {
            try {
                BlockingQueue<SysLogDTO> tempLogQueue = SpringUtil.getBean("logQueue");
                if (ObjectUtil.isNotEmpty(tempLogQueue)) {
                    Reference<ArrayList<SysLogDTO>> batchListWeakReference = ReferenceUtil.create(ReferenceUtil.ReferenceType.WEAK, new ArrayList<>(tempLogQueue));

                    LoggingFactory loggingFactory = SpringUtil.getBean(LoggingFactory.class);
                    loggingFactory.saveBatch(Objects.requireNonNull(batchListWeakReference.get()));
                    tempLogQueue.clear();
                }
            } catch (Exception e) {
                log.error("Saving Logs occurred error: {}", e.getMessage());
            } finally {
                System.gc();
            }
        }, 2, 1, TimeUnit.SECONDS, true);
    }
}
