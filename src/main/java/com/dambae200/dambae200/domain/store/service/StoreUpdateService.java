package com.dambae200.dambae200.domain.store.service;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.cigaretteList.domain.CigaretteList;
import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.domain.notification.service.storeNotification.StoreNotificationGeneratorAndSender;
import com.dambae200.dambae200.domain.notification.service.storeNotification.StoreNotificationType;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.domain.StoreBrand;
import com.dambae200.dambae200.domain.store.dto.StoreDto;
import com.dambae200.dambae200.domain.store.exception.DuplicateStoreException;
import com.dambae200.dambae200.domain.store.exception.InvalidStoreBrandCodeException;
import com.dambae200.dambae200.domain.store.repository.StoreRepository;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import com.dambae200.dambae200.global.common.DeleteResponse;
import com.dambae200.dambae200.global.common.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreUpdateService {

    final StoreRepository storeRepository;
    final UserRepository userRepository;
    final AccessRepository accessRepository;
    final CigaretteOnListRepository cigaretteOnListRepository;
    final RepoUtils repoUtils;
    final StoreNotificationGeneratorAndSender storeNotificationGeneratorAndSender;

    //TODO 영속화 관련 테스트
    @Transactional
    public StoreDto.GetResponse addStore(StoreDto.AddRequest request) throws EntityNotFoundException, InvalidStoreBrandCodeException, DuplicateStoreException {

        checkDuplicate(request.getName(), request.getStoreBrandCode());

        // Store 먼저 생성 후 영속화
        Store store = Store.builder()
                .brand(StoreBrand.ofCode(request.getStoreBrandCode()))
                .name(request.getName())
                .build();

        Store savedStore = storeRepository.save(store);

        // 연관 객체인 access 생성
        User admin = repoUtils.getOneElseThrowException(userRepository, request.getAdminId());
        Access access = Access.builder()
                .user(admin)
                .store(savedStore)
                .accessType(AccessType.ADMIN)
                .build();

        accessRepository.save(access);

        return new StoreDto.GetResponse(savedStore);
    }



    @Transactional
    public StoreDto.GetResponse updateStore(Long id, StoreDto.UpdateRequest request) throws EntityNotFoundException, InvalidStoreBrandCodeException, DuplicateStoreException {
        checkDuplicate(request.getName(), request.getStoreBrandCode());

        // 해당 엔티티 로드
        Store store = repoUtils.getOneElseThrowException(storeRepository, id);
        String oldName = store.toString();

        // 처리 (수정)
        StoreBrand brand = StoreBrand.ofCode(request.getStoreBrandCode());
        store.updateStore(request.getName(), brand);

        // 알림 발송
        storeNotificationGeneratorAndSender.generateAndSend(store, StoreNotificationType.STORE_UPDATED, oldName);

        // 리턴
        return new StoreDto.GetResponse(store);
    }

    @Transactional
    public DeleteResponse deleteStore(Long id) throws EntityNotFoundException{
        // 해당 엔티티 로드
        Store store = repoUtils.getOneElseThrowException(storeRepository, id);
        // 관련 엔티티 로드 및 삭제
        List<Access> accessList = accessRepository.findAllByStoreId(id);
        accessRepository.deleteAll(accessList);
        cigaretteOnListRepository.deleteAllByStoreId(id);

        //해당 엔티티 삭제
        storeRepository.delete(store);

        // 관련 알림 발송
        storeNotificationGeneratorAndSender.generateAndSend(store, StoreNotificationType.STORE_DELETED);

        // 리턴
        return new DeleteResponse("store", id);
    }

    private void checkDuplicate(String name, String brandCode){
        if(storeRepository.existsByNameAndBrand(name, StoreBrand.ofCode(brandCode))) {
            throw new DuplicateStoreException(name, brandCode);
        }
    }

}
