package com.dambae200.dambae200.domain.store.service;

import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.access.service.AccessService;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.domain.notification.service.storeNotification.StoreNotificationGeneratorAndSender;
import com.dambae200.dambae200.domain.notification.service.storeNotification.StoreNotificationType;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.domain.StoreBrand;
import com.dambae200.dambae200.domain.store.dto.StoreCreateRequest;
import com.dambae200.dambae200.domain.store.dto.StoreGetResponse;
import com.dambae200.dambae200.domain.store.dto.StoreUpdateRequest;
import com.dambae200.dambae200.domain.store.exception.DuplicatedStoreException;
import com.dambae200.dambae200.domain.store.exception.InvalidStoreBrandCodeException;
import com.dambae200.dambae200.domain.store.repository.StoreRepository;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.common.service.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreUpdateService {

    final StoreRepository storeRepository;
    final UserRepository userRepository;
    final AccessRepository accessRepository;
    final AccessService accessService;
    final CigaretteOnListRepository cigaretteOnListRepository;
    final RepoUtils repoUtils;
    final StoreNotificationGeneratorAndSender storeNotificationGeneratorAndSender;
    final StoreTransaction storeTransaction;

    public StoreGetResponse createStore(final StoreCreateRequest request){

        checkDuplicate(request.getName(), request.getStoreBrandCode());

        Store store = new Store(request.getName(), StoreBrand.ofCode(request.getStoreBrandCode()));

        Store saved = storeTransaction.createStoreTransaction(store, request.getAdminId());

        return new StoreGetResponse(saved);
    }


    public StoreGetResponse updateStore(final Long id, final StoreUpdateRequest request) throws EntityNotFoundException, InvalidStoreBrandCodeException, DuplicatedStoreException {
        checkDuplicate(request.getName(), request.getStoreBrandCode());

        // 해당 엔티티 로드
        Store store = repoUtils.getOneElseThrowException(storeRepository, id);
        String oldName = store.getFullname();

        // 처리 (수정)
        StoreBrand brand = StoreBrand.ofCode(request.getStoreBrandCode());
        store.updateStore(request.getName(), brand);
        storeRepository.save(store);

        // 알림 발송
        storeNotificationGeneratorAndSender.generateAndSend(store, StoreNotificationType.STORE_UPDATED, oldName);

        // 리턴
        return new StoreGetResponse(store);
    }


    public DeleteResponse deleteStore(final Long id){

        Store store = repoUtils.getOneElseThrowException(storeRepository, id);

        storeTransaction.deleteStoreTransaction(store);

        storeNotificationGeneratorAndSender.generateAndSend(store, StoreNotificationType.STORE_DELETED);

        return new DeleteResponse("store", id);
    }


    private void checkDuplicate(final String name, final String brandCode){
        if(storeRepository.existsByNameAndBrand(name, StoreBrand.ofCode(brandCode))) {
            throw new DuplicatedStoreException(name, brandCode);
        }
    }

}
