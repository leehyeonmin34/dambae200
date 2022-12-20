package com.dambae200.dambae200.domain.access.domain;

import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.global.common.dto.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Getter
@Table(name = "access")
public class Access extends BaseEntity {
    @Column(name = "access_type", nullable = false, updatable = true)
    @Convert(converter = AccessTypeConverter.class)
    private AccessType accessType;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;


    public Access(AccessType accessType, Store store, User user){
        this.accessType = accessType;
        this.store = store;
        this.user = user;
    }

    public void changeAccessType(AccessType accessType){
        this.accessType = accessType;
    }

}
