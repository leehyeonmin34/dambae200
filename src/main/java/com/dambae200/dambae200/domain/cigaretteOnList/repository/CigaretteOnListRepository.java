package com.dambae200.dambae200.domain.cigaretteOnList.repository;

import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CigaretteOnListRepository extends JpaRepository<CigaretteOnList, Long> {

    List<CigaretteOnList> findAllByCigaretteId(Long cigaretteId);

    void deleteAllByStore(Store store);

    boolean existsByStoreIdAndCigaretteId(Long storeId, Long cigaretteId);

    // fetch join으로 즉시 로딩
    @Query("select co from CigaretteOnList co join fetch co.store st " +
            "join fetch co.cigarette c " +
            "where st.id = :storeId " +
            "order by co.displayOrder asc")
    List<CigaretteOnList> findAllByStoreIdOrderByDisplay(@Param("storeId") Long storeId);

    @Query("select co from CigaretteOnList co join fetch co.store st " +
            "join fetch co.cigarette c " +
            "where st.id = :storeId")
    List<CigaretteOnList> findAllByStoreId(@Param("storeId") Long storeId);
}
