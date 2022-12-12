package com.dambae200.dambae200.global.scheduler.service;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Component
public class CustomThreadPoolTaskScheduler extends ThreadPoolTaskScheduler {
// 기존 ThreadPoolTaskScheduler는 정의된 주기가 0이면 예외를 발생시킨다
// 이 클래스는 예외를 발생시키지 않도록 했다.

    private static final long serialVersionUID = 1L;

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
        if (period <= 0) {
            return null;
        }
        ScheduledFuture<?> future = super.scheduleAtFixedRate(task, period);
        return future;
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
        if (period <= 0) {
            return null;
        }
        ScheduledFuture<?> future = super.scheduleAtFixedRate(task, startTime, period);
        return future;
    }

}
