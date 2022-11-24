package com.dambae200.dambae200.domain.cigarette.service;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteDto;
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

    public CigaretteDto.GetListResponse findAllCigarettes() {
        List<Cigarette> cigarettes = cigaretteRepository.findAll();
        return new CigaretteDto.GetListResponse(cigarettes);
    }

    public CigaretteDto.GetListResponse findAllByOfficialNameLike(String name) {
        List<Cigarette> cigarettes = cigaretteRepository.findAllByOfficialNameLike("%" + name + "%");
        return new CigaretteDto.GetListResponse(cigarettes);
    }



    public CigaretteDto.GetListResponse findAll() {
        List<Cigarette> cigarettes = cigaretteRepository.findAll();
        return new CigaretteDto.GetListResponse(cigarettes);
    }

}
