package dambae200.dambae200.domain.cigaretteOnList.repository;

import dambae200.dambae200.domain.cigaretteList.domain.CigaretteList;
import dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public class CigaretteOnListRepositoryImpl implements CigaretteOnListRepositoryCustom {
    private final EntityManager em;

    @Override
    public CigaretteOnList findOneByIdCustom(Long id) {
        return em.find(CigaretteOnList.class, id);
    }

    /*
    @Override
    public void updateOrderByMovement(Long cigaretteListId, int selectedCigaretteOrder, int n) {
    }
     */
}
