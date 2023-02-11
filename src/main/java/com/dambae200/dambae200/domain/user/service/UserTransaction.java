package com.dambae200.dambae200.domain.user.service;

import com.dambae200.dambae200.domain.access.repository.AccessRepository;
import com.dambae200.dambae200.domain.notification.repository.NotificationRepository;
import com.dambae200.dambae200.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserTransaction {

    private AccessRepository accessRepository;
    private NotificationRepository notificationRepository;
    private UserRepository userRepository;

    public void deleteById(Long id){
        accessRepository.deleteAllByUserId(id);
        notificationRepository.deleteAllByUserId(id);

        // 삭제
        userRepository.deleteById(id);
    }
}
