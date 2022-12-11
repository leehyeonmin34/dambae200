package com.dambae200.dambae200.domain.cigaretteOnList.service.scheduler;

import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.global.config.CacheEnv;
import com.dambae200.dambae200.global.service.CacheModule;
import com.dambae200.dambae200.global.service.SchedulerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;


// scheduler의 start, remove 로직을 담당
//@AllArgsConstructor
//@Slf4j
//public class CigaretteOnListFlushingScheduler {
//
//    final private SchedulerService schedulerService;
//    final private Runnable task;
////    final private String taskId;
//    final private Long period;
//
//    public void start(String taskId) {
//        log.info(String.format("%s 스케줄러가 시작되었습니다.", taskId));
//        schedulerService.register(
//                taskId,
//                task,
//                period);
//    }
//
//    public void remove() {
//
//        schedulerService.remove(taskId);
//        log.info(String.format("%s 스케줄러가 삭제되었습니다.", taskId));
//    }
//
//}
