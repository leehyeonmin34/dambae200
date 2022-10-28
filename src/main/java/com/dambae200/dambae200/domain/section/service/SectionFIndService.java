package dambae200.dambae200.domain.section.service;

import dambae200.dambae200.domain.section.domain.Section;
import dambae200.dambae200.domain.section.dto.SectionDto;
import dambae200.dambae200.domain.section.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionFIndService {

    private final SectionRepository sectionRepository;

    public SectionDto.GetListResponse findAll() {
        List<Section> sections = sectionRepository.findAll();
        return new SectionDto.GetListResponse(sections);
    }

    public SectionDto.GetListResponse findAllByCigaretteListId(Long listId){
        List<Section> sections = sectionRepository.findAllByCigaretteListId(listId);
        return new SectionDto.GetListResponse(sections);
    }

    public SectionDto.GetListResponse findAllOrderByOrder() {
        List<Section> sections = sectionRepository.findAllByOrder();
        return new SectionDto.GetListResponse(sections);
    }


}
