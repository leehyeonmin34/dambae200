package dambae200.dambae200.domain.cigarette.repository;

import dambae200.dambae200.domain.cigarette.domain.Cigarette;

public interface CigaretteRepositoryCustom {
    Cigarette findOneByIdCustom(Long id);
}
