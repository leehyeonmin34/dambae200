package com.dambae200.dambae200.domain.cigaretteOnList.repository;

import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CigaretteOnListRepository extends JpaRepository<CigaretteOnList, Long>, CigaretteOnListRepositoryCustom {
    List<CigaretteOnList> findAllByCigaretteId(Long cigaretteId);
    void deleteAllByStoreId(Long storeId);


    //List<CigaretteOnList> findAllByStoreId(Long storeId);


    boolean existsByStoreIdAndCigaretteId(Long storeId, Long cigaretteId);

    @Query("select co from CigaretteOnList co join fetch co.store st " +
            "join fetch co.cigarette c " +
            "where st.id = :storeId " +
            "order by co.displayOrder asc")
    List<CigaretteOnList> findAllByStoreIdOrderByDisplay(@Param("storeId") Long storeId);

    @Query("select co from CigaretteOnList co join fetch co.store st " +
            "join fetch co.cigarette c " +
            "where st.id = :storeId " +
            "order by co.computerizedOrder asc")
    List<CigaretteOnList> findAllByStoreIdOrderByComputerized(@Param("storeId") Long storeId);


    @Query("select co from CigaretteOnList co join fetch co.store st " +
            "join fetch co.cigarette c " +
            "where st.id = :storeId")
    List<CigaretteOnList> findAllByStoreId(@Param("storeId") Long storeId);


    @Query("select co from CigaretteOnList co join fetch co.cigarette c " +
            "join fetch co.store st " +
            "where st.id = :storeId and c.officialName < :name")
    List<CigaretteOnList> findAllBySmallThanOfficialName(@Param("storeId") Long storeId, @Param("name") String name);

    @Query("select co from CigaretteOnList co where co.id in :ids ")
    List<CigaretteOnList> findAllByInculdeIds(@Param("ids") List<Long> ids);

    @Query("select co from CigaretteOnList co join fetch co.store st " +
            "where  st.id = :storeId and co.displayOrder > :order")
    List<CigaretteOnList> findAllByStoreIdAndBiggerOrder(@Param("storeId") Long storeId, @Param("order") int order);

    @Modifying(clearAutomatically = true)
    @Query("update CigaretteOnList co set co.computerizedOrder=co.computerizedOrder+1 where co.store.id = :storeId and co.computerizedOrder >= :order")
    void updateByOrderGreaterThanPlus(@Param("storeId") Long storeId, @Param("order") int order);

    @Modifying(clearAutomatically = true)
    @Query("update CigaretteOnList co set co.computerizedOrder=:order where co.id=:cigaretteOnListId")
    void updateComputerizedOrder(@Param("cigaretteOnListId") Long cigaretteOnListId, @Param("order") int order);

    @Modifying(clearAutomatically = true)
    @Query("update CigaretteOnList co set co.displayOrder=:order where co.id=:cigaretteOnListId")
    void updateDisplayOrder(@Param("cigaretteOnListId") Long cigaretteOnListId, @Param("order") int order);

    @Modifying(clearAutomatically = true)
    @Query("update CigaretteOnList co set co.displayOrder=co.displayOrder+1 where co.store.id = :storeId and co.displayOrder >= :afterOrder and co.displayOrder < :draggalbeOrder")
    void dragAndDropDisplayOrderUp(@Param("storeId") Long storeId, @Param("afterOrder") int afterOrder, @Param("draggalbeOrder") int draggalbeOrder);

    @Modifying(clearAutomatically = true)
    @Query("update CigaretteOnList co set co.displayOrder=co.displayOrder-1 where co.store.id = :storeId and co.displayOrder < :afterOrder and co.displayOrder > :draggalbeOrder")
    void dragAndDropDisplayOrderDown(@Param("storeId") Long storeId, @Param("afterOrder") int afterOrder, @Param("draggalbeOrder") int draggalbeOrder);

    @Modifying(clearAutomatically = true)
    @Query("update CigaretteOnList co set co.computerizedOrder=co.computerizedOrder+1 where co.store.id = :storeId and co.computerizedOrder >= :afterOrder and co.computerizedOrder < :draggalbeOrder")
    void dragAndDropComputerizedOrderUp(@Param("storeId") Long storeId, @Param("afterOrder") int afterOrder, @Param("draggalbeOrder") int draggalbeOrder);

    @Modifying(clearAutomatically = true)
    @Query("update CigaretteOnList co set co.computerizedOrder=co.computerizedOrder-1 where co.store.id = :storeId and co.computerizedOrder < :afterOrder and co.computerizedOrder > :draggalbeOrder")
    void dragAndDropComputerizedOrderDown(@Param("storeId") Long storeId, @Param("afterOrder") int afterOrder, @Param("draggalbeOrder") int draggalbeOrder);


    /*
      @Query("select co from CigaretteOnList co join fetch co.cigarette c " +
            "join fetch co.store st " +
            "where st.id = :storeId and c.officialName like %:name%")
    List<CigaretteOnList> findAllByOfficialNameLike(@Param("storeId") Long storeId, @Param("name") String name);

    @Query("select co from CigaretteOnList co join fetch co.cigarette c " +
            "join fetch co.store st " +
            "where st.id = :storeId and c.officialName not like %:name%")
    List<CigaretteOnList> findAllByNotInstudeOfficialNameLike(@Param("storeId") Long storeId, @Param("name") String name);
     */
}
