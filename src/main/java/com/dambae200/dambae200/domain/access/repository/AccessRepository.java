package com.dambae200.dambae200.domain.access.repository;

import com.dambae200.dambae200.domain.access.domain.Access;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessRepository extends JpaRepository<Access, Long> {
    List<Access> findAllByUserId(Long userId);
    List<Access> findAllByStoreId(Long storeId);
    void deleteAllByUserId(Long userId);
}
