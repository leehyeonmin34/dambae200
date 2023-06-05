package com.dambae200.dambae200.domain.cigarette.domain;


import com.dambae200.dambae200.global.common.dto.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
public class Cigarette extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name ="cigarette_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "official_name", nullable = false, updatable = true, unique = true)
    private String officialName;

    @Column(name = "simple_name", nullable = false, updatable = true, unique = false)
    private String simpleName;

    @Column(name = "file_path_medium", nullable = false, updatable = true, unique = true)
    private String filePathMedium;

    @Column(name = "file_path_large", nullable = false, updatable = true, unique = true)
    private String filePathLarge;

    @Column(name = "vertical", nullable = false, updatable = true, unique = false)
    private boolean vertical;


    public void updateCigarette(String officialName, String simpleName) {
        this.officialName = officialName;
        this.simpleName = simpleName;
    }
}
