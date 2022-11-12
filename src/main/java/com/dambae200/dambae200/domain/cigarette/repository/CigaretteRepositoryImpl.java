package dambae200.dambae200.domain.cigarette.repository;

import dambae200.dambae200.domain.cigarette.domain.Cigarette;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public class CigaretteRepositoryImpl implements CigaretteRepositoryCustom {

    private final EntityManager em;

    @Override
    public Cigarette findOneByIdCustom(Long id) {
        return em.find(Cigarette.class, id);
    }
}
