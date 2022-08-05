package com.dambae200.dambae200.domain.store.repository;

import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.domain.StoreBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    public List<Store> findByNameLike(String s);
    public boolean existsByNameAndBrand(String name, StoreBrand brandCode);
//    public boolean existsByIdAndAccessType(Long id, AccessType accessType);
}
