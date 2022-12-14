package com.dambae200.dambae200.domain.cigaretteOnList.domain;


import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.global.common.dto.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
//@ToString
@ToString(callSuper = true)
public class CigaretteOnList extends BaseEntity{

    private static final long serialVersionUID = -455965069912515404L;

    @Id
    @GeneratedValue
    @Column(name = "cigaretteOnList_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cigarette_id")
    private Cigarette cigarette;

    @Column(name = "count", nullable = true, updatable = true)
    private int count;

    private int displayOrder;

    private int computerizedOrder;

    private String customizedName;


//    public void changeCigaretteList(CigaretteList cigaretteList) {
//        if (this.cigaretteList != null) {
//            this.cigaretteList.getCigaretteOnLists().remove(this);
//        }
//        this.cigaretteList = cigaretteList;
//    }
    public void changeStore(Store store) {
        this.store = store;
    }


    private void changeCigarette(Cigarette cigarette) {
        this.cigarette = cigarette;
    }

    public void changeDisplayOrder(int displayOrder){this.displayOrder = displayOrder;}

    public void changeComputerizedOrder(int computerizedOrder){
        this.computerizedOrder = computerizedOrder;}

    public void changeOrderInfo(int displayOrder, int computerizedOrder){
        changeDisplayOrder(displayOrder);
        changeComputerizedOrder(computerizedOrder);
    }

    public void changeCount(int count) {
        this.count = count;
    }

    public void changeCustomizedName(String name){
        this.customizedName = name;
    }

    //생성 메서드
    public static CigaretteOnList createCigaretteOnList(Store store, Cigarette cigarette, String customizedName) {
        CigaretteOnList cigaretteOnList = new CigaretteOnList();
        cigaretteOnList.changeStore(store);
        cigaretteOnList.changeCigarette(cigarette);
        //cigaretteOnList.cigarette.updateCigaretteSimpleName(simpleName);
        cigaretteOnList.customizedName = customizedName;
        cigaretteOnList.changeCount(-1); // 빈 값은 -1로 표현.

        return cigaretteOnList;
    }
}
