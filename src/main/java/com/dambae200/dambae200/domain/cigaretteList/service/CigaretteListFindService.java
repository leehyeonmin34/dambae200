//package com.dambae200.dambae200.domain.cigaretteList.service;
//
//import com.dambae200.dambae200.domain.cigaretteList.domain.CigaretteList;
//import com.dambae200.dambae200.domain.cigaretteList.dto.CigaretteListDto;
//import com.dambae200.dambae200.domain.cigaretteList.repository.CigaretteListRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CigaretteListFindService {
//
//    private final CigaretteListRepository cigaretteListRepository;
//
//    public CigaretteListDto.GetResponse findByStoreId(Long stordId) {
//        CigaretteList cigaretteList = cigaretteListRepository.findByStoreId(stordId);
//
//        return new CigaretteListDto.GetResponse(cigaretteList);
//    }
//
//
//}
