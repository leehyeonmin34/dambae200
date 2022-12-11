package com.dambae200.dambae200.domain.access.repository;

import com.dambae200.dambae200.domain.access.domain.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dambae200.dambae200.domain.access.domain.AccessType;


import java.util.List;
import java.util.Optional;

public interface AccessRepository extends JpaRepository<Access, Long> {
    List<Access> findAllByUserId(Long userId);
    List<Access> findAllByStoreId(Long storeId);
    void deleteAllByUserId(Long userId);
    Optional<Access> findByUserIdAndStoreId(Long userId, Long storeId);
    boolean existsByUserIdAndStoreId(Long userId, Long storeId);
    boolean existsByStoreIdAndAccessType(Long storeId, AccessType accessType);
//    @Query(value =
//            "SELECT A.id, A.createdAt, A.updatedAt, A.accessType, A.store, A.user " +
//                "FROM Access A " +
//                    "INNER JOIN Access B " +
//                        "ON A.store = B.store " +
//                "WHERE B.id = :accessId and A.accessType.code = " + "'AT04'")
//    Optional<Access> findAdminAccessByStaffAccessId(@Param("accessId") Long accessId);
}
