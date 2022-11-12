package dambae200.dambae200.domain.cigaretteOnList.repository;

import dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CigaretteOnListRepository extends JpaRepository<CigaretteOnList, Long>, CigaretteOnListRepositoryCustom {
    List<CigaretteOnList> findAllByCigaretteId(Long cigaretteId);

    //List<CigaretteOnList> findAllByCigaretteListId(Long cigaretteListId);


    boolean existsByCigaretteListIdAndCigaretteId(Long cigaretteListId, Long cigaretteId);

    @Query("select co from CigaretteOnList co join fetch co.cigaretteList cl " +
            "join fetch co.cigarette c " +
            "where cl.id = :cigaretteListId " +
            "order by co.displayOrder asc")
    List<CigaretteOnList> findAllByCigaretteListIdOrderByDisplay(@Param("cigaretteListId") Long cigaretteListId);

    @Query("select co from CigaretteOnList co join fetch co.cigaretteList cl " +
            "join fetch co.cigarette c " +
            "where cl.id = :cigaretteListId " +
            "order by co.computerizedOrder asc")
    List<CigaretteOnList> findAllByCigaretteListIdOrderByComputerized(@Param("cigaretteListId") Long cigaretteListId);


    @Query("select co from CigaretteOnList co join fetch co.cigaretteList cl " +
            "join fetch co.cigarette c " +
            "where cl.id = :cigaretteListId")
    List<CigaretteOnList> findAllByCigaretteListId(@Param("cigaretteListId") Long cigaretteListId);


    @Query("select co from CigaretteOnList co join fetch co.cigarette c " +
            "join fetch co.cigaretteList cl " +
            "where cl.id = :cigaretteListId and c.officialName < :name")
    List<CigaretteOnList> findAllBySmallThanOfficialName(@Param("cigaretteListId") Long cigaretteListId, @Param("name") String name);

    @Query("select co from CigaretteOnList co where co.id in :ids ")
    List<CigaretteOnList> findAllByInculdeIds(@Param("ids") List<Long> ids);

    @Query("select co from CigaretteOnList co join fetch co.cigaretteList cl " +
            "where  cl.id = :cigaretteListId and co.displayOrder > :order")
    List<CigaretteOnList> findAllByCigaretteListIdAndBiggerOrder(@Param("cigaretteListId") Long cigaretteListId, @Param("order") int order);

    @Modifying(clearAutomatically = true)
    @Query("update CigaretteOnList co set co.computerizedOrder=co.computerizedOrder+1 where co.cigaretteList.id = :cigaretteListId and co.computerizedOrder >= :order")
    void updateByOrderGreaterThanPlus(@Param("cigaretteListId") Long cigaretteListId, @Param("order") int order);

    @Modifying(clearAutomatically = true)
    @Query("update CigaretteOnList co set co.computerizedOrder=:order where co.id=:cigaretteOnListId")
    void updateComputerizedOrder(@Param("cigaretteOnListId") Long cigaretteOnListId, @Param("order") int order);

    @Modifying(clearAutomatically = true)
    @Query("update CigaretteOnList co set co.displayOrder=:order where co.id=:cigaretteOnListId")
    void updateDisplayOrder(@Param("cigaretteOnListId") Long cigaretteOnListId, @Param("order") int order);

    @Modifying(clearAutomatically = true)
    @Query("update CigaretteOnList co set co.displayOrder=co.displayOrder+1 where co.cigaretteList.id = :cigaretteListId and co.displayOrder >= :afterOrder and co.displayOrder < :draggalbeOrder")
    void dragAndDropDisplayOrderUp(@Param("cigaretteListId") Long cigaretteListId, @Param("afterOrder") int afterOrder, @Param("draggalbeOrder") int draggalbeOrder);

    @Modifying(clearAutomatically = true)
    @Query("update CigaretteOnList co set co.displayOrder=co.displayOrder-1 where co.cigaretteList.id = :cigaretteListId and co.displayOrder < :afterOrder and co.displayOrder > :draggalbeOrder")
    void dragAndDropDisplayOrderDown(@Param("cigaretteListId") Long cigaretteListId, @Param("afterOrder") int afterOrder, @Param("draggalbeOrder") int draggalbeOrder);

    @Modifying(clearAutomatically = true)
    @Query("update CigaretteOnList co set co.computerizedOrder=co.computerizedOrder+1 where co.cigaretteList.id = :cigaretteListId and co.computerizedOrder >= :afterOrder and co.computerizedOrder < :draggalbeOrder")
    void dragAndDropComputerizedOrderUp(@Param("cigaretteListId") Long cigaretteListId, @Param("afterOrder") int afterOrder, @Param("draggalbeOrder") int draggalbeOrder);

    @Modifying(clearAutomatically = true)
    @Query("update CigaretteOnList co set co.computerizedOrder=co.computerizedOrder-1 where co.cigaretteList.id = :cigaretteListId and co.computerizedOrder < :afterOrder and co.computerizedOrder > :draggalbeOrder")
    void dragAndDropComputerizedOrderDown(@Param("cigaretteListId") Long cigaretteListId, @Param("afterOrder") int afterOrder, @Param("draggalbeOrder") int draggalbeOrder);


    /*
      @Query("select co from CigaretteOnList co join fetch co.cigarette c " +
            "join fetch co.cigaretteList cl " +
            "where cl.id = :cigaretteListId and c.officialName like %:name%")
    List<CigaretteOnList> findAllByOfficialNameLike(@Param("cigaretteListId") Long cigaretteListId, @Param("name") String name);

    @Query("select co from CigaretteOnList co join fetch co.cigarette c " +
            "join fetch co.cigaretteList cl " +
            "where cl.id = :cigaretteListId and c.officialName not like %:name%")
    List<CigaretteOnList> findAllByNotIncludeOfficialNameLike(@Param("cigaretteListId") Long cigaretteListId, @Param("name") String name);
     */
}
