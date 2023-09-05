package com.dambae200.dambae200.domain.store.service;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.repository.StoreRepository;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import com.dambae200.dambae200.global.common.service.RepoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class StoreTransaction {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final AccessRepository accessRepository;
    private final RepoUtils repoUtils;
    private final CigaretteOnListRepository cigaretteOnListRepository;

    @Transactional
    public Store createStoreTransaction(Store store, Long adminId){
        Store savedStore = storeRepository.save(store);

        // 연관 엔티티 생성
        User admin = repoUtils.getOneElseThrowException(userRepository, adminId);
        Access access = new Access(AccessType.ADMIN, savedStore, admin);
        accessRepository.save(access);

        return savedStore;
    }


    @Transactional
    public void deleteStoreTransaction(final Store store){

        // 관련 엔티티 삭제
        accessRepository.deleteAllByStore(store);
        cigaretteOnListRepository.deleteAllByStore(store);

        //해당 엔티티 삭제
        storeRepository.delete(store);
    }

}
