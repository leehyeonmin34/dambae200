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

    //List<CigaretteOnList> findAllByCigaretteListIdAndCustomizedNameLike(Long cigaretteListId, String s);

    List<CigaretteOnList> findAllByCigaretteListIdAndCustomizedNameNotLike(Long cigaretteListId, String s);

    boolean existsByCigaretteListIdAndCigaretteId(Long cigaretteListId, Long cigaretteId);

    @Query("select co from CigaretteOnList co join fetch co.cigaretteList cl " +
            "join fetch co.cigarette c " +
            "where cl.id = :cigaretteListId " +
            "order by co.displayOrder")
    List<CigaretteOnList> findAllByCigaretteListIdOrderByDisplay(@Param("cigaretteListId") Long cigaretteListId);

    @Query("select co from CigaretteOnList co join fetch co.cigaretteList cl " +
            "join fetch co.cigarette c " +
            "where cl.id = :cigaretteListId " +
            "order by co.computerizedOrder")
    List<CigaretteOnList> findAllByCigaretteListIdOrderByComputerized(@Param("cigaretteListId") Long cigaretteListId);

    @Query("select co from CigaretteOnList co join fetch co.cigaretteList cl " +
            "join fetch co.cigarette c " +
            "where cl.id = :cigaretteListId")
    List<CigaretteOnList> findAllByCigaretteListId(@Param("cigaretteListId") Long cigaretteListId);


    @Query("select co from CigaretteOnList co join fetch co.cigarette c " +
            "join fetch co.cigaretteList cl " +
            "where cl.id = :cigaretteListId and c.officialName like %:name%")
    List<CigaretteOnList> findAllByOfficialNameLike(@Param("cigaretteListId") Long cigaretteListId, @Param("name") String name);

    @Query("select co from CigaretteOnList co join fetch co.cigarette c " +
            "join fetch co.cigaretteList cl " +
            "where cl.id = :cigaretteListId and c.officialName not like %:name%")
    List<CigaretteOnList> findAllByNotIncludeOfficialNameLike(@Param("cigaretteListId") Long cigaretteListId, @Param("name") String name);

    @Query("select co from CigaretteOnList co where co.id in :ids ")
    List<CigaretteOnList> findAllByInculdeIds(@Param("ids") List<Long> ids);

    @Query("select co from CigaretteOnList co join fetch co.cigaretteList cl " +
            "where  cl.id = :cigaretteListId and co.displayOrder > :order")
    List<CigaretteOnList> findAllByCigaretteListIdAndBiggerOrder(@Param("cigaretteListId") Long cigaretteListId, @Param("order") int order);



    /*
    @Modifying
    @Query("update CigaretteOnList co set co.order = co.order + :n where co.cigaretteList.id = :cigaretteListId and co.order >= :selectedCigaretteOrder")
    void updateOrderByMovement(@Param("cigaretteListId") Long cigaretteListId, @Param("order") int selectedCigaretteOrder, @Param("n") int n);

    @Query("select co from CigaretteOnList co join fetch co.cigaretteList cl " +
            "join fetch co.section s " +
            "where cl.id = :cigaretteListId and s.id = :sectionId")
    List<CigaretteOnList> findCigaretteOnListBySectionId(@Param("cigaretteListId") Long cigaretteListId,@Param("sectionID") Long sectionID);



    @Query("select co from CigaretteOnList co join fetch co.cigarette c " +
            "join fetch co.cigaretteList cl " +
            "where cl.id = :cigaretteListId and c.customizedName not like %:name%")
    List<CigaretteOnList> findAllByNotIncludeCustomizedNameLike(@Param("cigaretteListId") Long cigaretteListId, @Param("name") String name);
     */
}
