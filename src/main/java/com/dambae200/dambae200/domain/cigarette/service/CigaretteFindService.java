package com.dambae200.dambae200.domain.cigarette.service;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteGetListResponse;
import com.dambae200.dambae200.domain.cigarette.repository.CigaretteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CigaretteFindService {

    private final CigaretteRepository cigaretteRepository;

    public CigaretteGetListResponse findAllCigarettes() {
        List<Cigarette> cigarettes = cigaretteRepository.findAll();
        return new CigaretteGetListResponse(cigarettes);
    }

    public CigaretteGetListResponse findAllByOfficialNameLike(String name) {
        List<Cigarette> cigarettes = cigaretteRepository.findAllByOfficialNameLike("%" + name + "%");
        return new CigaretteGetListResponse(cigarettes);
    }



    public CigaretteGetListResponse findAll() {
        List<Cigarette> cigarettes = cigaretteRepository.findAll();
        return new CigaretteGetListResponse(cigarettes);
    }

}
