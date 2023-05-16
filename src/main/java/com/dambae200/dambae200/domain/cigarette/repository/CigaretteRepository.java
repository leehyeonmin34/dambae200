package com.dambae200.dambae200.domain.cigarette.repository;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CigaretteRepository extends JpaRepository<Cigarette, Long> {
    boolean existsByOfficialName(String name);
}