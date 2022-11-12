package com.dambae200.dambae200.domain.access.service;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.domain.access.dto.AccessDto;
import com.dambae200.dambae200.domain.access.exception.DuplicateAccessApply;
import com.dambae200.dambae200.domain.access.exception.InvalidAccessTypeCodeException;
import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.notification.exception.CannotFindAccessNotificationType;
import com.dambae200.dambae200.domain.notification.service.NotificationSender;
import com.dambae200.dambae200.domain.notification.service.accessNotification.AccessNotificationGenerator;
import com.dambae200.dambae200.domain.notification.service.accessNotification.AccessNotificationGeneratorAndSender;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.dto.StoreDto;
import com.dambae200.dambae200.domain.store.repository.StoreRepository;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import com.dambae200.dambae200.global.common.DeleteResponse;
import com.dambae200.dambae200.global.common.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.text.html.parser.Entity;
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

    @Transactional
    public AccessDto.GetListUserResponse findAllByStoreId(Long storeId){
        List<Access> accessList = accessRepository.findAllByStoreId(storeId);
        return new AccessDto.GetListUserResponse(accessList);
    }


    @Transactional
    public AccessDto.GetStoreListResponse findAllByUserId(Long userId){

        final List<AccessDto.GetStoreResponse> accessList = accessRepository.findAllByUserId(userId).stream().map(access -> {
            final boolean applicatorExists = applicatorExists(access);
            return new AccessDto.GetStoreResponse(access, applicatorExists);
        }).collect(Collectors.toList());

        final AccessDto.GetStoreListResponse response = AccessDto.GetStoreListResponse.builder()
                .accesses(accessList)
                .total(accessList.size())
                .build();

        return response;
    }


    // 내가 관리자인(ADMIN access) Store라면, 지원자(WAITING access)가 있는지 여부 리턴
    private boolean applicatorExists(Access access){
        if (access.getAccessType().equals(AccessType.ADMIN))
            return accessRepository.existsByStoreIdAndAccessType(access.getStore().getId(), AccessType.WAITING);
        else return false;
    }

    @Transactional
    public AccessDto.GetResponse applyAccess(AccessDto.ApplyRequest request){
        Long userId = request.getUserId();
        Long storeId = request.getStoreId();
        if(accessRepository.existsByUserIdAndStoreId(userId, storeId))
            throw new DuplicateAccessApply(userId, storeId);

        // 관련 엔티티 로드
        Store store = repoUtils.getOneElseThrowException(storeRepository, request.getStoreId());
        User user = repoUtils.getOneElseThrowException(userRepository, request.getUserId());

        // 액세스 생성 및 WAITING 상태 설정
        Access access = Access.builder()
                .store(store)
                .user(user)
                .accessType(AccessType.WAITING)
                .build();
        Access saved = accessRepository.save(access);

        // 관련 알림 생성 및 전송
        accessNotificationGeneratorAndSender.from(AccessType.INACCESSIBLE, access, false);

        return new AccessDto.GetResponse(saved);

    }

    @Transactional
    public AccessDto.GetResponse updateAccess(Long id, String accessTypeCode, boolean byAdmin){
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
        return new AccessDto.GetResponse(access);
    }

    @Transactional
    private Access findAdminAccessByStaffAccessId(Long id) throws RuntimeException{
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
    public DeleteResponse deleteAccess(Long id) throws EntityNotFoundException{

        // 일반 스태프(ACCESSIBLE access)가 권한을 철회하는 거라면 목록 관리자에게 알림을 보내야 하기 때문에 알림용 정보 추출
        Access access = repoUtils.getOneElseThrowException(accessRepository, id);
        boolean isDeletedByAccessibleStaff = access.getAccessType().equals(AccessType.ACCESSIBLE);

        // 삭제
        repoUtils.deleteOneElseThrowException(accessRepository, id);

        // 관련 알림 생성 및 전송
        if(isDeletedByAccessibleStaff) {
            access.changeAccessType(AccessType.INACCESSIBLE);
            accessNotificationGeneratorAndSender.from(AccessType.ACCESSIBLE, access, false);
            System.out.println("알림 보내져야합니다~~~~");
        }
        // 리턴
        return new DeleteResponse("access", id);
    }

}
