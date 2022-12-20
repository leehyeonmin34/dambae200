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
    @Column(name = "cigarette_on_list_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false, updatable = false, unique = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "cigarette_id", nullable = true, updatable = true, unique = false)
    private Cigarette cigarette;

    @Column(name = "count", nullable = true, updatable = true, unique = false)
    private int count;

    @Column(name = "display_order", nullable = true, updatable = true, unique = false)
    private int displayOrder;

    @Column(name = "computerized_order", nullable = true, updatable = true, unique = false)
    private int computerizedOrder;

    @Column(name = "customized_name", nullable = true, updatable = true, unique = false)
    private String customizedName;

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

    //생성자
    public CigaretteOnList(Store store, Cigarette cigarette, String customizedName) {
        this.store = store;
        this.cigarette = cigarette;
        this.customizedName = customizedName;
        this.count = -1; // 빈 값은 -1로 표현
    }
}
