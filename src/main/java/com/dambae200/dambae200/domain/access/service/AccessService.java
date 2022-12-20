package com.dambae200.dambae200.domain.access.service;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.domain.access.dto.*;
import com.dambae200.dambae200.domain.access.exception.AccessNotAllowedException;
import com.dambae200.dambae200.domain.access.exception.DuplicatedAccessApply;
import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.notification.service.accessNotification.AccessNotificationGeneratorAndSender;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.repository.StoreRepository;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import com.dambae200.dambae200.global.cache.service.CacheModule;
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.common.service.RepoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccessService {

    final RepoUtils repoUtils;
    final AccessRepository accessRepository;
    final StoreRepository storeRepository;
    final UserRepository userRepository;
    final AccessNotificationGeneratorAndSender accessNotificationGeneratorAndSender;
    final EntityManager em;
    final CacheModule cacheModule;

    @Transactional
    public AccessGetUserListResponse findAllByStoreId(Long storeId){
        List<Access> accessList = accessRepository.findAllByStoreId(storeId);
        return new AccessGetUserListResponse(accessList);
    }

    @Transactional
    public AccessGetStoreListResponse findAllByUserId(Long userId){

        final List<AccessGetStoreResponse> accessList = accessRepository.findAllByUserId(userId).stream().map(access -> {
            final boolean applicatorExists = applicatorExists(access);
            return new AccessGetStoreResponse(access, applicatorExists);
        }).collect(Collectors.toList());

        final AccessGetStoreListResponse response = new AccessGetStoreListResponse(accessList);
        return response;
    }

    // 내가 관리자인(ADMIN access) Store라면, 지원자(WAITING access)가 있는지 여부 리턴
    private boolean applicatorExists(Access access){
        if (access.getAccessType().equals(AccessType.ADMIN))
            return accessRepository.existsByStoreIdAndAccessType(access.getStore().getId(), AccessType.WAITING);
        else return false;
    }

    @Transactional
    public AccessGetResponse applyAccess(AccessApplyRequest request){
        Long userId = request.getUserId();
        Long storeId = request.getStoreId();
        if(accessRepository.existsByUserIdAndStoreId(userId, storeId))
            throw new DuplicatedAccessApply(userId, storeId);

        // 관련 엔티티 로드
        Store store = repoUtils.getOneElseThrowException(storeRepository, request.getStoreId());
        User user = repoUtils.getOneElseThrowException(userRepository, request.getUserId());

        // 액세스 생성 및 WAITING 상태 설정
        Access access = new Access(AccessType.WAITING, store, user);
        Access saved = accessRepository.save(access);

        // 관련 알림 생성 및 전송
        accessNotificationGeneratorAndSender.from(AccessType.INACCESSIBLE, access, false);

        return new AccessGetResponse(saved);

    }

    @Transactional
    public AccessGetResponse updateAccess(Long id, String accessTypeCode, boolean byAdmin){
        // 해당 엔티티 로드
        Access access = repoUtils.getOneElseThrowException(accessRepository, id);

        // 관리자 권한 양도일 경우, 관리자의 권한을 ACCESSIBLE로 변경함
        if(accessTypeCode.equals(AccessType.ADMIN.getCode())){
            Access adminUserAccess = findAdminAccessByStaffAccessId(id);
            adminUserAccess.changeAccessType(AccessType.ACCESSIBLE);
        }

        // 처리 (엔티티 업데이트)
        AccessType prev = access.getAccessType();
        AccessType newType = AccessType.ofCode(accessTypeCode);
        access.changeAccessType(newType);

        // 관련 알림 생성 및 전송
        accessNotificationGeneratorAndSender.from(prev, access, byAdmin);

        // 리턴
        return new AccessGetResponse(access);
    }

    @Transactional
    private Access findAdminAccessByStaffAccessId(Long id){
        TypedQuery<Access> query = em.createQuery(
                "SELECT A " +
                "FROM Access A " +
                "INNER JOIN Access B " +
                "ON A.store = B.store " +
                "WHERE B.id = :accessId  and A.accessType = " + "'AT04'", Access.class)
                .setParameter("accessId", id);

        Access result = query.getSingleResult();
        if (result == null) throw new RuntimeException(
                "accessId" + id + "에 해당하는 관리자 access 엔티티를 찾을 수 없습니다."
        );
        return result;
    }

    @Transactional
    public void checkAccess(Long userId, Long storeId){

        if(userId == null || storeId == null)
            throw new AccessNotAllowedException(userId, storeId, AccessType.ACCESSIBLE);

        // 해당 entity 찾기. 없으면 권한이 없으므로 접근 불가
        Access access = accessRepository.findByUserIdAndStoreId(userId, storeId)
                .orElseThrow(() ->  new AccessNotAllowedException(userId, storeId, AccessType.ACCESSIBLE));

        // 접근 불가 상태거나 신청이 승인 대기중이라면 접근 불가
        switch (access.getAccessType()){
            case INACCESSIBLE:
            case WAITING:
                throw new AccessNotAllowedException(userId, storeId, AccessType.ACCESSIBLE);
        }
    }

    @Transactional
    public void checkAdminAccess(Long userId, Long storeId){

        if(userId == null || storeId == null)
            throw new AccessNotAllowedException(userId, storeId, AccessType.ACCESSIBLE);

        // 해당 entity 찾기. 없으면 권한이 없으므로 접근 불가
        Access access = accessRepository.findByUserIdAndStoreId(userId, storeId)
                .orElseThrow(() ->  new AccessNotAllowedException(userId, storeId, AccessType.ADMIN));

        // 해당 권한이 '관리자' 타입이 아니라면 접근 불가
        if(!access.getAccessType().equals(AccessType.ADMIN))
            throw new AccessNotAllowedException(userId, storeId, AccessType.ADMIN);
    }


    @Transactional
    public DeleteResponse deleteAccess(Long id){

        // 일반 스태프(ACCESSIBLE access)가 권한을 철회하는 거라면 목록 관리자에게 알림을 보내야 하기 때문에 알림용 정보 추출
        Access access = repoUtils.getOneElseThrowException(accessRepository, id);
        boolean isDeletedByAccessibleStaff = access.getAccessType().equals(AccessType.ACCESSIBLE);

        // 삭제
        repoUtils.deleteOneElseThrowException(accessRepository, id);

        // 관련 알림 생성 및 전송
        if(isDeletedByAccessibleStaff) {
            access.changeAccessType(AccessType.INACCESSIBLE);
            accessNotificationGeneratorAndSender.from(AccessType.ACCESSIBLE, access, false);
        }
        // 리턴
        return new DeleteResponse("access", id);
    }



}
