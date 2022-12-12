package com.dambae200.dambae200.domain.cigarette.domain;


import com.dambae200.dambae200.global.common.dto.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
public class Cigarette extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name ="cigarette_id")
    private Long id;

    @Column(name = "officialName")
    private String officialName;

    @Column(name = "simpleName")
    private String simpleName;

    private String filePathMedium;

    private String filePathLarge;

    private boolean vertical;



    public void updateCigarette(String officialName, String simpleName) {
        this.officialName = officialName;
        this.simpleName = simpleName;
    }


//    public void updateCigaretteOfficialName(String name) {
//        this.officialName = name;
//    }
//
//    public void updateCigarettesimpleName(String name) {
//        this.simpleName = name;
//    }
}
