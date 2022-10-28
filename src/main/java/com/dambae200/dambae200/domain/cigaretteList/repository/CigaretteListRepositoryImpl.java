package dambae200.dambae200.domain.cigaretteList.repository;

import dambae200.dambae200.domain.cigaretteList.domain.CigaretteList;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public class CigaretteListRepositoryImpl implements CigaretteListRepositoryCustom {

    private final EntityManager em;

    @Override
    public CigaretteList findOneByIdCustom(Long id) {
        return em.find(CigaretteList.class, id);
    }

}
