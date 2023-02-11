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
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.common.service.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessService {

    private final RepoUtils repoUtils;
    private final AccessRepository accessRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final AccessNotificationGeneratorAndSender accessNotificationGeneratorAndSender;
    private final EntityManager em;
    private final AccessTransaction accessTransaction;

    @Transactional(readOnly = true)
    public AccessGetUserListResponse findAllByStoreId(final Long storeId){
        final List<Access> accessList = accessRepository.findAllByStoreId(storeId);
        return new AccessGetUserListResponse(accessList);
    }

    // TODO - JOIN으로 DB조회 횟수 줄일 수 있으려나
    @Transactional(readOnly = true)
    public AccessGetStoreListResponse findAllByUserId(final Long userId){

        final List<AccessGetStoreResponse> accessList = accessRepository.findAllByUserId(userId).stream().map(access -> {
            final boolean applicatorExists = applicatorExists(access);
            return new AccessGetStoreResponse(access, applicatorExists);
        }).collect(Collectors.toList());

        final AccessGetStoreListResponse response = new AccessGetStoreListResponse(accessList);
        return response;
    }

    // 내가 관리자인(ADMIN access) Store라면, 지원자(WAITING access)가 있는지 여부 리턴
    private boolean applicatorExists(final Access access){
        if (access.getAccessType().equals(AccessType.ADMIN))
            return accessRepository.existsByStoreIdAndAccessType(access.getStore().getId(), AccessType.WAITING);
        else return false;
    }

    // 중간에 실패해도 롤백할 필요 없음
    public void applyAccess(final AccessApplyRequest request){
        Long userId = request.getUserId();
        Long storeId = request.getStoreId();

        if(accessRepository.existsByUserIdAndStoreId(userId, storeId))
            throw new DuplicatedAccessApply(userId, storeId);

        // 연관관계 설정용 엔티티 생성
        final Store store = repoUtils.getOneElseThrowException(storeRepository, storeId);
        final User user = repoUtils.getOneElseThrowException(userRepository, userId);

        // WAITING 액세스 생성 및 저장
        // TODO - 중복 등록 방지책 필요(unique 대리키 활용)
        Access access = new Access(AccessType.WAITING, store, user);

        // 생성 및 알림 전송
        accessTransaction.saveAccessAndSendNoti(AccessType.INACCESSIBLE, access, false);

    }

    @Transactional
    public AccessGetResponse updateAccess(final Long id, final String accessTypeCode, final boolean byAdmin){
        // 해당 엔티티 로드
        final Access access = repoUtils.getOneElseThrowException(accessRepository, id);

        // 관리자 권한 양도일 경우, 관리자의 권한을 ACCESSIBLE로 변경함
        if(accessTypeCode.equals(AccessType.ADMIN.getCode())){
            final Access adminUserAccess = accessRepository.findAdminAccessByStaffAccessId(id)
                    .orElseThrow(() -> new EntityNotFoundException("accessId" + id + "에 해당하는 관리자 access 엔티티를 찾을 수 없습니다."));
            adminUserAccess.changeAccessType(AccessType.ACCESSIBLE);
        }

        // 처리 (엔티티 업데이트)
        final AccessType prev = access.getAccessType();
        final AccessType newType = AccessType.ofCode(accessTypeCode);
        access.changeAccessType(newType);

        // 관련 알림 생성 및 전송
        accessNotificationGeneratorAndSender.from(prev, access, byAdmin);

        // 리턴
        return new AccessGetResponse(access);
    }


    @Transactional(readOnly = true)
    public void checkAccess(final Long userId, final Long storeId){

        if(userId == null || storeId == null)
            throw new AccessNotAllowedException(userId, storeId, AccessType.ACCESSIBLE);

        // 해당 entity 찾기. 없으면 권한이 없으므로 접근 불가
        final Access access = accessRepository.findByUserIdAndStoreId(userId, storeId)
                .orElseThrow(() ->  new AccessNotAllowedException(userId, storeId, AccessType.ACCESSIBLE));

        // 접근 불가 상태거나 신청이 승인 대기중이라면 접근 불가
        switch (access.getAccessType()){
            case INACCESSIBLE:
            case WAITING:
                throw new AccessNotAllowedException(userId, storeId, AccessType.ACCESSIBLE);
        }
    }

    @Transactional(readOnly = true)
    public void checkAdminAccess(final Long userId, final Long storeId){

        if(userId == null || storeId == null)
            throw new AccessNotAllowedException(userId, storeId, AccessType.ACCESSIBLE);

        // 해당 entity 찾기. 없으면 권한이 없으므로 접근 불가
        final Access access = accessRepository.findByUserIdAndStoreId(userId, storeId)
                .orElseThrow(() ->  new AccessNotAllowedException(userId, storeId, AccessType.ADMIN));

        // 해당 권한이 '관리자' 타입이 아니라면 접근 불가
        if(!access.getAccessType().equals(AccessType.ADMIN))
            throw new AccessNotAllowedException(userId, storeId, AccessType.ADMIN);
    }


    @Transactional
    public DeleteResponse deleteAccess(final Long id){

        // 일반 스태프(ACCESSIBLE access)가 권한을 철회하는 거라면 목록 관리자에게 알림을 보내야 하기 때문에 알림용 정보 추출
        final Access access = repoUtils.getOneElseThrowException(accessRepository, id);
        final boolean IsAccessibleStaffDeleted = access.getAccessType().equals(AccessType.ACCESSIBLE);

        // 삭제
        repoUtils.deleteOneElseThrowException(accessRepository, id);

        // 관련 알림 생성 및 전송
        if(IsAccessibleStaffDeleted) {
            access.changeAccessType(AccessType.INACCESSIBLE);
            accessNotificationGeneratorAndSender.from(AccessType.ACCESSIBLE, access, false);
        }
        // 리턴
        return new DeleteResponse("access", id);
    }



}
