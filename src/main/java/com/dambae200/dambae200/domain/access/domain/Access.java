package com.dambae200.dambae200.domain.access.domain;

import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.global.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Getter
@Table(name = "access")
public class Access extends BaseEntity {
    @Convert(converter = AccessTypeConverter.class)
    private AccessType accessType;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Access(AccessType accessType, Store store, User user){
        this.accessType = accessType;
        this.store = store;
        this.user = user;
    }

    public void changeAccessType(AccessType accessType){
        this.accessType = accessType;
    }

}
