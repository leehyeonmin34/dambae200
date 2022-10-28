package dambae200.dambae200.domain.section.domain;

import dambae200.dambae200.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Section extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "section_id")
    private Long id;

    private String name;

    private int sectionOrder;

    public void updateName(String name) {
        this.name = name;
    }


    public void changeOrder(int order) {
        this.sectionOrder = order;
    }

    public Section(String name) {
        this.name = name;
    }


}
