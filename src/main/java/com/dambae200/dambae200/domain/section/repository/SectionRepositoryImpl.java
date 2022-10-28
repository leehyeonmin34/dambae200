package dambae200.dambae200.domain.section.repository;

import dambae200.dambae200.domain.section.domain.Section;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class SectionRepositoryImpl implements SectionRepositoryCustom {

    private final EntityManager em;

    @Override
    public Section findOneByIdCustom(Long id) {
        return em.find(Section.class, id);
    }

    @Override
    public List<Section> findAllByOrder(){
        return em.createQuery("select s from Section s order by s.sectionOrder asc").getResultList();
    }

}
