package org.kaws.common.constant;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author Bosco
 * @date 2022/4/9 7:24 下午
 */

@Slf4j
public class BaseConstant {

    private static final Integer LOG_EXECUTOR_CORE_POOL_SIZE = 1;

    private static final String LOG_SINGLE_EXECUTOR_NAME_PREFIX = "LOG-single-thread-";

    public static final ScheduledThreadPoolExecutor LOG_EXECUTOR;

    static {
        ThreadFactory threadFactory = ThreadUtil.newNamedThreadFactory(LOG_SINGLE_EXECUTOR_NAME_PREFIX, ThreadUtil.currentThreadGroup(),true, (t, e) -> log.error("子线程执行异常，线程名:{}，异常信息:{}", t.getName(), e.getMessage()));
        LOG_EXECUTOR = new ScheduledThreadPoolExecutor(LOG_EXECUTOR_CORE_POOL_SIZE, threadFactory);
    }

}
