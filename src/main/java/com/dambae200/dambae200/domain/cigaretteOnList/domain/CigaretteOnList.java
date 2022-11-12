package com.dambae200.dambae200.domain.cigaretteOnList.domain;


import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigaretteList.domain.CigaretteList;
import com.dambae200.dambae200.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@ToString(callSuper = true)
public class CigaretteOnList extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cigaretteOnList_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cigaretteList_id")
    private CigaretteList cigaretteList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cigarette_id")
    private Cigarette cigarette;

    private int count;

    private int displayOrder;

    private int computerizedOrder;

    private String customizedName;


    public void changeCigaretteList(CigaretteList cigaretteList) {
        if (this.cigaretteList != null) {
            this.cigaretteList.getCigaretteOnLists().remove(this);
        }
        this.cigaretteList = cigaretteList;
    }


    private void changeCigarette(Cigarette cigarette) {
        this.cigarette = cigarette;
    }

    public void changeDisplayOrder(int displayOrder){this.displayOrder = displayOrder;}

    public void changeComputerizedOrder(int computerizedOrder){
        this.computerizedOrder = computerizedOrder;}

    public void changeCount(int count) {
        this.count = count;
    }

    //생성 메서드
    public static CigaretteOnList createCigaretteOnList(Cigarette cigarette, String customizedName) {
        CigaretteOnList cigaretteOnList = new CigaretteOnList();
        cigaretteOnList.changeCigarette(cigarette);
        //cigaretteOnList.cigarette.updateCigaretteSimpleName(simpleName);
        cigaretteOnList.customizedName = customizedName;
        cigaretteOnList.changeCount(0); //등록할때는 0으로

        return cigaretteOnList;
    }
}
