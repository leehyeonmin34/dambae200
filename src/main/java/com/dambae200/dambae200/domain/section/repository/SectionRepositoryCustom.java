package dambae200.dambae200.domain.section.repository;

import dambae200.dambae200.domain.section.domain.Section;

import java.util.List;

public interface SectionRepositoryCustom {

    Section findOneByIdCustom(Long id);

    List<Section> findAllByOrder();
}
