package com.dambae200.dambae200.domain.access.service;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.domain.access.exception.DuplicatedAccessApply;
import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.notification.service.accessNotification.AccessNotificationGeneratorAndSender;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.repository.StoreRepository;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import com.dambae200.dambae200.global.common.service.RepoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccessTransaction {

    private final AccessRepository accessRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final RepoUtils repoUtils;

    private final AccessNotificationGeneratorAndSender accessNotificationGeneratorAndSender;


    @Transactional
    public void saveAccessAndSendNoti(AccessType prevAccessType, Access access, boolean byAdmin){
        Access saved = accessRepository.save(access);
        log.info(saved.toString());
        accessNotificationGeneratorAndSender.from(prevAccessType, saved, byAdmin);
    }

}
