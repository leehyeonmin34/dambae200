package dambae200.dambae200.domain.cigaretteList.domain;

import dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import dambae200.dambae200.domain.store.domain.Store;
import dambae200.dambae200.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@ToString(callSuper = true)
public class CigaretteList extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cigaretteList_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "store_id")
    private Store store;

    private String name;

    @OneToMany(mappedBy = "cigaretteList", cascade = CascadeType.ALL)
    private List<CigaretteOnList> cigaretteOnLists = new ArrayList<>();


    //연관관계 메서드
    public void changeStore(Store store) {
        this.store = store;
        store.changeCigaretteList(this);
    }

    public void changeName(String name) {
        this.name = name;
    }


    public void addCigaretteOnList(CigaretteOnList cigaretteOnList) {
        cigaretteOnLists.add(cigaretteOnList);
        cigaretteOnList.changeCigaretteList(this);
    }

    public void deleteCigaretteOnList(CigaretteOnList cigaretteOnList) {
        cigaretteOnLists.remove(cigaretteOnList);
    }

    //생성메서드
    public static CigaretteList createCigaretteList(Store store) {
        CigaretteList cigaretteList = new CigaretteList();
        cigaretteList.changeStore(store);
        cigaretteList.name = store.getName();

        return cigaretteList;
    }
}
