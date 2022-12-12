package com.dambae200.dambae200.domain.cigaretteOnList.service.scheduler;

import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.global.service.CacheModule;
import com.dambae200.dambae200.global.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

@Component
@Slf4j
public class CigaretteOnListStopFlushingSchedulerManager {

    final private CigaretteOnListFlushingSchedulerManager cigaretteOnListFlushingSchedulerManager;
    final private CigaretteOnListRepository cigaretteOnListRepository;
    final private SchedulerService schedulerService;
    final private CacheModule cacheModule;

    final private UnaryOperator<List<CigaretteOnList>> dbWriteFunction;
    final private Long period;


    public CigaretteOnListStopFlushingSchedulerManager(CigaretteOnListFlushingSchedulerManager cigaretteOnListFlushingSchedulerManager, CigaretteOnListRepository cigaretteOnListRepository, SchedulerService schedulerService, CacheModule cacheModule) {
        this.cigaretteOnListFlushingSchedulerManager = cigaretteOnListFlushingSchedulerManager;
        this.cigaretteOnListRepository = cigaretteOnListRepository;
        this.schedulerService = schedulerService;
        this.cacheModule = cacheModule;

        this.dbWriteFunction = cigaretteOnListRepository::saveAll;
        this.period = TimeUnit.SECONDS.toMillis(31);
    }

    public void start(Long storeId) {

        String taskId = getTaskId(storeId);
        log.info(String.format("%s 스케줄러가 시작되었습니다.", taskId));
        schedulerService.registerLazyExec(
                taskId,
                () -> remove(storeId),
                period);
    }

    public void startIfNotStarted(Long storeId){
        if(!existsByStoreId(storeId))
            cigaretteOnListFlushingSchedulerManager.startIfNotStarted(storeId);
            start(storeId);
    }

    public void remove(Long storeId) {
        String taskId = getTaskId(storeId);
        schedulerService.remove(taskId);
        cigaretteOnListFlushingSchedulerManager.remove(storeId);
    }

    public String getTaskId(Long storeId){
        return "cigarette-on-list-stop-flushing-scheduler-of-storeId-" + storeId;
    }

    public boolean existsByStoreId(Long storeId){
        String taskId = getTaskId(storeId);
        return schedulerService.existsByTaskId(taskId);
    }

}
