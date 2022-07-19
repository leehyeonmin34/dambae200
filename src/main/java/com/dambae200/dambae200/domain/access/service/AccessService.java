package com.dambae200.dambae200.domain.access.service;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.domain.access.dto.AccessDto;
import com.dambae200.dambae200.domain.access.exception.InvalidAccessTypeCodeException;
import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.notification.exception.CannotFindAccessNotificationType;
import com.dambae200.dambae200.domain.notification.service.NotificationSender;
import com.dambae200.dambae200.domain.notification.service.accessNotification.AccessNotificationGenerator;
import com.dambae200.dambae200.domain.notification.service.accessNotification.AccessNotificationGeneratorAndSender;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.repository.StoreRepository;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import com.dambae200.dambae200.global.common.DeleteResponse;
import com.dambae200.dambae200.global.common.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessService {

    final RepoUtils repoUtils;
    final AccessRepository accessRepository;
    final StoreRepository storeRepository;
    final UserRepository userRepository;
    final AccessNotificationGeneratorAndSender accessNotificationGeneratorAndSender;

    public AccessDto.GetListUserResponse findAllByStoreId(Long storeId){
        List<Access> accessList = accessRepository.findAllByStoreId(storeId);
        return new AccessDto.GetListUserResponse(accessList);
    }

    public AccessDto.GetResponse addAccess(AccessDto.ApplyRequest request) throws EntityNotFoundException, CannotFindAccessNotificationType{
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

    public AccessDto.GetResponse updateAccess(Long id, String accessTypeCode, Boolean byAdmin) throws EntityNotFoundException, InvalidAccessTypeCodeException, CannotFindAccessNotificationType {
        // 해당 엔티티 로드
        Access access = repoUtils.getOneElseThrowException(accessRepository, id);

        // 처리 (엔티티 업데이트)
        AccessType prev = access.getAccessType();
        AccessType newType = AccessType.ofCode(accessTypeCode);
        access.changeAccessType(newType);

        // 관련 알림 생성 및 전송
        accessNotificationGeneratorAndSender.from(prev, access, byAdmin);

        // 리턴
        return new AccessDto.GetResponse(access);

    }

    public DeleteResponse deleteAccess(Long id) throws EntityNotFoundException{
        // 삭제
        repoUtils.deleteOneElseThrowException(accessRepository, id);

        // 리턴
        return new DeleteResponse("access", id);
    }

}
