package dambae200.dambae200.domain.section.service;

import dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import dambae200.dambae200.domain.section.domain.Section;
import dambae200.dambae200.domain.section.dto.SectionDto;
import dambae200.dambae200.domain.section.exception.DuplicateSectionException;
import dambae200.dambae200.domain.section.repository.SectionRepository;
import dambae200.dambae200.global.common.DeleteResponse;
import dambae200.dambae200.global.common.RepoUtils;
import dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionUpdateService {

    private final SectionRepository sectionRepository;
    private final CigaretteOnListRepository cigaretteOnListRepository;
    private final RepoUtils repoUtils;

    public SectionDto.GetResponse addSection(SectionDto.SectionRequest request) throws DuplicateSectionException {
        //중복검사
        checkDuplicate(request.getName());

        Section section = new Section(request.getName());
        //order처리필요

        Section savedSection = sectionRepository.save(section);

        return new SectionDto.GetResponse(savedSection);
    }

    public SectionDto.GetResponse updateSection(Long id, SectionDto.SectionRequest request) throws EntityNotFoundException, DuplicateSectionException {

        checkDuplicate(request.getName());

        Section findSection = repoUtils.getOneElseThrowException(sectionRepository, id);
        findSection.updateName(request.getName());

        return new SectionDto.GetResponse(findSection);
    }

    public DeleteResponse deleteSection(Long id) throws EntityNotFoundException {
        Section section = repoUtils.getOneElseThrowException(sectionRepository, id);
        List<CigaretteOnList> cigaretteOnLists = cigaretteOnListRepository.findAllBySectionId(id);

        for (CigaretteOnList cigaretteOnList : cigaretteOnLists) {
            cigaretteOnList.setSectionWithNull();
        }

        sectionRepository.delete(section);
        return new DeleteResponse("section", id);
    }

    //순서변경



    private void checkDuplicate(String name) {
        if (sectionRepository.existsByName(name)) {
            throw new DuplicateSectionException();
        }
    }
}
