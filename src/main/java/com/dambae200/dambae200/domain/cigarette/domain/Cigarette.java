package dambae200.dambae200.domain.cigarette.domain;


import dambae200.dambae200.global.common.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Cigarette extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name ="cigarette_id")
    private Long id;

    @Column(name = "officialName")
    private String officialName;

    @Column(name = "simpleName")
    private String simpleName;

    @Builder
    public Cigarette(String officialName, String simpleName) {
        this.officialName = officialName;
        this.simpleName = simpleName;
    }

    public void updateCigarette(String officialName, String simpleName) {
        this.officialName = officialName;
        this.simpleName = simpleName;
    }

    public void updateCigaretteOfficialName(String name) {
        this.officialName = name;
    }

    public void updateCigarettesimpleName(String name) {
        this.simpleName = name;
    }
}
