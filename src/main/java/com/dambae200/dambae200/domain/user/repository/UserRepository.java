package com.dambae200.dambae200.domain.user.repository;

import com.dambae200.dambae200.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    public Boolean existsByEmail(String email);

    public Boolean existsByNickname(String nickname);
}
