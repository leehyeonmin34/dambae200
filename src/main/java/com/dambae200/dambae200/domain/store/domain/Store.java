package com.dambae200.dambae200.domain.store.domain;

import com.dambae200.dambae200.global.common.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Getter
@Table(name = "store")
public class Store extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "brand", nullable = false)
    @Convert(converter = StoreBrandConverter.class)
    private StoreBrand brand;

    @OneToOne(mappedBy = "store",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CigaretteList cigaretteList;

    @Builder
    public Store(String name, StoreBrand brand){
        this.name = name;
        this.brand = brand;
    }

    public void updateStore(String name, StoreBrand brand){
        this.name = name;
        this.brand = brand;
    }

    public String getFullname(){
        return this.brand.getDesc() + " " + this.name;
    }

    public void changeCigaretteList(CigaretteList cigaretteList) {
        this.cigaretteList = cigaretteList;
    }


}
