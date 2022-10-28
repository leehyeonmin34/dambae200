package dambae200.dambae200.domain.section.repository;

import dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import dambae200.dambae200.domain.section.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long>, SectionRepositoryCustom {
    boolean existsByName(String name);

    @Query("select co from CigaretteOnList co join fetch co.cigaretteList cl " +
            "where  cl.id = :cigaretteListId and co.order > :order")
    List<CigaretteOnList> findAllByCigaretteListIdAndBiggerOrder(@Param("cigaretteListId") Long cigaretteListId, @Param("order") int order);

    @Query("select distinct co.section from CigaretteOnList co join fetch co.cigaretteList cl " +
            "join fetch co.section s " +
            "where cl.id = :cigaretteListId")
    List<Section> findAllByCigaretteListId(@Param("cigaretteListId") Long cigaretteListId);
}
