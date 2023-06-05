package com.dambae200.dambae200.domain.store.domain;

import com.dambae200.dambae200.global.common.dto.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Getter
@Table(name = "store")
public class Store extends BaseEntity {

    @Column(name = "name", nullable = false, updatable = true, unique = false)
    private String name;

    @Column(name = "brand", nullable = false, updatable = true, unique = false)
    @Convert(converter = StoreBrandConverter.class)
    private StoreBrand brand;

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

    public Store(Long id){
        this.id = id;
    }


}
