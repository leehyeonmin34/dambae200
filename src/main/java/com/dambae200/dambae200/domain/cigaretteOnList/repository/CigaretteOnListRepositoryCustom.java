package dambae200.dambae200.domain.cigaretteOnList.repository;

import dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;

public interface CigaretteOnListRepositoryCustom {
    CigaretteOnList findOneByIdCustom(Long id);
    //void updateOrderByMovement(Long cigaretteListId, int selectedCigaretteOrder, int n);
}
