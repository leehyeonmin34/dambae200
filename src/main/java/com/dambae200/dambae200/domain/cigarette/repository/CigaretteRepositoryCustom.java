package com.dambae200.dambae200.domain.cigarette.repository;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;

public interface CigaretteRepositoryCustom {
    Cigarette findOneByIdCustom(Long id);
}
