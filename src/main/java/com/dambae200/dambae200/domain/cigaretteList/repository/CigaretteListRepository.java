package dambae200.dambae200.domain.cigaretteList.repository;

import dambae200.dambae200.domain.cigaretteList.domain.CigaretteList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CigaretteListRepository extends JpaRepository<CigaretteList, Long>, CigaretteListRepositoryCustom {
    CigaretteList findByStoreId(Long storeId);
}
