package dambae200.dambae200.domain.cigarette.repository;

import dambae200.dambae200.domain.cigarette.domain.Cigarette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CigaretteRepository extends JpaRepository<Cigarette, Long>, CigaretteRepositoryCustom {
    List<Cigarette> findAllByOfficialNameLike(String s);

    boolean existsByOfficialName(String name);

    @Query("select c from Cigarette c where c.officialName = : officialName")
    Cigarette findOneByOfficialName(@Param("officialName") String officialName);

    @Query("select c from Cigarette c where c.customizedName = : customizedName")
    Cigarette findOneByCustomizedName(@Param("customizedName") String customizedName);
}
