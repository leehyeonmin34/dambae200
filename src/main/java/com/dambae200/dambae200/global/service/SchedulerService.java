package com.dambae200.dambae200.global.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final TaskScheduler customThreadPoolTaskScheduler;

    private Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public void register(String taskId, Runnable task, Long period) {
        ScheduledFuture<?> scheduledTask = customThreadPoolTaskScheduler.scheduleAtFixedRate(task, period);
        scheduledTasks.put(taskId, scheduledTask);
    }

    public void register(String taskId, Runnable task, Date startDate, Long period) {
        ScheduledFuture<?> scheduledTask = customThreadPoolTaskScheduler.scheduleAtFixedRate(task, startDate, period);
        scheduledTasks.put(taskId, scheduledTask);
    }

    // initialDelay는 Millisecond 단위
    public void register(String taskId, Runnable task, Long initialDelay, Long period) {

        Date startDate = Timestamp.valueOf(LocalDateTime.now().plusSeconds(TimeUnit.MILLISECONDS.toSeconds(initialDelay)));
        register(taskId, task, startDate, period);
    }

    public void registerLazyExec(String taskId, Runnable task, Long period) {
        register(taskId, task, period, period);
    }

    public void remove(String taskId) {
        if(scheduledTasks.get(taskId) != null) {
            scheduledTasks.get(taskId).cancel(true);
            scheduledTasks.remove(taskId);
            log.info(taskId + "가 종료되었습니다.");
        }
    }

    public boolean existsByTaskId(String taskId){
        return scheduledTasks.containsKey(taskId);
    }


}
