package com.dambae200.dambae200.domain.store.domain;

import com.dambae200.dambae200.global.common.BaseEntity;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

//    public void changeCigaretteList(CigaretteList cigaretteList) {
//        this.cigaretteList = cigaretteList;
//    }


}
