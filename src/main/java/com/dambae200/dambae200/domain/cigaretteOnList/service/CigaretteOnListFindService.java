package com.dambae200.dambae200.domain.cigaretteOnList.service;

import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListGetListResponse;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.global.cache.config.CacheEnv;
import com.dambae200.dambae200.global.cache.service.HashCacheModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CigaretteOnListFindService {

    private final CigaretteOnListRepository cigaretteOnListRepository;
    private final HashCacheModule hashCacheModule;

    //리스트에 있는 담배 보여주기(진열순서)
    public CigaretteOnListGetListResponse findAllByStoreIdOrderByDisplay(Long storeId, Long requestUserId) {

        List<CigaretteOnList> cigaretteOnLists = hashCacheModule.getAllCacheOrLoad(CacheEnv.CIGARETTE_LIST, storeId,
        cigaretteOnListRepository::findAllByStoreIdOrderByDisplay, CigaretteOnList::getId)
                .values().stream().collect(Collectors.toList());
        cigaretteOnLists.sort(Comparator.comparingInt(CigaretteOnList::getDisplayOrder));

        return new CigaretteOnListGetListResponse(cigaretteOnLists);
    }

    //리스트에 있는 담배 보여주기(전산순서)
    public CigaretteOnListGetListResponse findAllByStoreIdOrderByComputerized(Long cigaretteListId, Long requestUserId) {
        List<CigaretteOnList> cigaretteOnLists = cigaretteOnListRepository.findAllByStoreIdOrderByComputerized(cigaretteListId);
        return new CigaretteOnListGetListResponse(cigaretteOnLists);
    }

}
