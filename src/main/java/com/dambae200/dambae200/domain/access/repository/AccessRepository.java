package com.dambae200.dambae200.domain.access.repository;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.store.domain.Store;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dambae200.dambae200.domain.access.domain.AccessType;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface AccessRepository extends JpaRepository<Access, Long> {
    List<Access> findAllByUserId(Long userId);
    List<Access> findAllByStoreId(Long storeId);
    void deleteAllByStore(Store store);
    void deleteAllByUserId(Long userId);
    Optional<Access> findByUserIdAndStoreId(Long userId, Long storeId);
    boolean existsByUserIdAndStoreId(Long userId, Long storeId);
    boolean existsByStoreIdAndAccessType(Long storeId, AccessType accessType);

    @Query( "SELECT A " +
            "FROM Access A " +
            "INNER JOIN Access B " +
            "ON A.store = B.store " +
            "WHERE B.id = :accessId  and A.accessType = " + "'AT04'")
    Optional<Access> findAdminAccessByStaffAccessId(@Param("accessId") Long accessId);
}
