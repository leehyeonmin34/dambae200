package com.dambae200.dambae200.domain.notification.service.storeNotification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreNotificationType {

    // STORE
    // (storeName, oldStoreName)
    STORE_DELETED("매장 삭제됨",
          "%1$s 매장이 삭제되었습니다",
          "해당 매장에 대한 데이터가 삭제되어 더 이상 이용할 수 없습니다."),
    STORE_UPDATED("매장 업데이트됨",
          "%1$s 매장 정보가 변경되었습니다",
          "%1$s 매장의 이름이 %2$s로 변경되었습니다.")
    ;

    private String desc;
    private String titleTemplate;
    private String contentTemplate;

    public String getTitleFrom(String... strings){
        return String.format(this.getTitleTemplate(), strings);
    }
    public String getContentFrom(String... strings){
        return String.format(this.getContentTemplate(), strings);
    }


}
