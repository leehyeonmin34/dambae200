package com.dambae200.dambae200.domain.cigaretteOnList.service.scheduler;

import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.global.cache.config.CacheEnvOld;
import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.scheduler.service.SchedulerService;
import com.dambae200.dambae200.global.cache.service.SetCacheModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

// scheduler 생성에 필요한 요소들을 관리
@Component
@Slf4j
public class CigaretteOnListFlushingSchedulerManager {
    final private CigaretteOnListRepository cigaretteOnListRepository;
    final private SchedulerService schedulerService;
    final private SetCacheModule setCacheModule;

    final private UnaryOperator<List<CigaretteOnList>> dbWriteFunction;
    final private Long period;


    public CigaretteOnListFlushingSchedulerManager(CigaretteOnListRepository cigaretteOnListRepository, SchedulerService schedulerService, SetCacheModule setCacheModule) {
        this.cigaretteOnListRepository = cigaretteOnListRepository;
        this.schedulerService = schedulerService;
        this.setCacheModule = setCacheModule;

        this.dbWriteFunction = cigaretteOnListRepository::saveAll;
        this.period = TimeUnit.SECONDS.toMillis(10);
    }

    public void start(Long storeId) {

        String taskId = getTaskId(storeId);

        schedulerService.registerLazyExec(
                taskId,
                task(storeId),
                period);
        log.info(String.format("%s 스케줄러가 시작되었습니다.", taskId));
    }

    private Runnable task(Long storeId){
        return () -> {
            setCacheModule.flushAll(CacheType.CIGARETTE_DIRTY, storeId, dbWriteFunction);
            log.info(String.format("%s task가 수행됐습니다.", getTaskId(storeId)));
        };
    }

    public void startIfNotStarted(Long storeId){
        if(!existsByStoreId(storeId))
            start(storeId);

    }

    public void remove(Long storeId) {
        String taskId = getTaskId(storeId);
        schedulerService.remove(taskId);
    }

    public String getTaskId(Long storeId){
        return "cigarette-on-list-flushing-scheduler-of-storeId-" + storeId;
    }

    public boolean existsByStoreId(Long storeId){
        String taskId = getTaskId(storeId);
        return schedulerService.existsByTaskId(taskId);
    }




}
