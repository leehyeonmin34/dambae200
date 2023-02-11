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
@ToString(callSuper = true)
public class CigaretteOnList extends BaseEntity{

    private static final long serialVersionUID = -455965069912515404L;

    @Id
    @GeneratedValue
    @Column(name = "cigarette_on_list_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false, updatable = false, unique = false)
    private Store store;

    @ManyToOne(fetch = FetchType.EAGER)
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


    public void changeOrderInfo(int displayOrder, int computerizedOrder){
        this.displayOrder = displayOrder;
        this.computerizedOrder = computerizedOrder;
    }

    public void changeCount(int count) {
        this.count = count;
    }

    public void changeCustomizedName(String name){
        this.customizedName = name;
    }

    public static class Builder{

        private Store store;
        private Cigarette cigarette;
        private int count;
        private int displayOrder;
        private int computerizedOrder;
        private String customizedName;

        public Builder(Store store, int listSize){
            this.store = store;
            this.displayOrder = listSize + 1;
            this.computerizedOrder = listSize + 1;
            this.count = -1; // 빈 값을 -1로 표현
        }

        public Builder cigarette(Cigarette cigarette){
            this.cigarette = cigarette;
            return this;
        }

        public Builder customizedName(String customizedName){
            this.customizedName = customizedName;
            return this;
        }

        public CigaretteOnList build(){
            return new CigaretteOnList(this);
        }

    }

    public CigaretteOnList(Builder builder){
        this.store = builder.store;
        this.cigarette = builder.cigarette;
        this.count = builder.count;
        this.displayOrder = builder.displayOrder;
        this.computerizedOrder = builder.computerizedOrder;
        this.customizedName = builder.customizedName;
    }
}
