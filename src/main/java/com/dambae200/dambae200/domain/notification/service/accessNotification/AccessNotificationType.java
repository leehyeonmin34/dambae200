package com.dambae200.dambae200.domain.notification.service.accessNotification;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public enum AccessNotificationType {
    // ACCESS
    // to User
    // (storeName)
    ACCESS_APPLY("접근 신청 결과 대기중",
            "%1$s 매장에 접근 요청을 보냈습니다",
            "관리자가 승인하면 해당 매장의 담배 목록에 접근 가능합니다.",
            false),
    ACCESS_DENIED("접근 신청 거절됨",
            "%1$s 매장에 신청한 접근 요청이 거절되었습니다",
            "관리자가 거절해서 해당 매장의 담배 목록에 접근이 불가합니다.",
            false),
    ACCESS_APPROVED("접근 신청 승인됨",
            "%1$s 매장에 신청한 접근 요청이 승인되었습니다",
            "지금부터 해당 매장의 담배 목록에 접근 가능합니다.",
            false),
    ACCESS_ADMIN_PROMOTION("관리자로 승급됨",
            "%1$s 매장의 관리자로 승격되었습니다",
            "지금부터 해당 매장의 정보를 편집 가능하고 사용자들의 접근권한을 관리할 수 있습니다.",
            false),
    ACCESS_DELETED("접근 권한 제거됨",
            "%1$s 매장에 대한 접근 권한이 제거되었습니다",
            "해당 매장의 데이터를 더 이상 이용할 수 없습니다.",
            false),

    // to Admin
    // (storeName, userName)
    APPLLICATION_CAME("접근 신청 요청 도착함"
    ,"%1$s 매장에 접근 요청이 도착했습니다",
            "사용자 %2$s(이)가 %1$s 매장에 접근 신청을 했습니다.",
            true),
    APPLICATION_CANCELD("접근 신청 취소됨"
            ,"회원님이 관리중이신 %1$s 매장에 접수되었던 요청이 취소되었습니다.",
            "사용자 %2$s(이)가 %1$s 매장에 대한 접근 신청을 취소하였습니다.",
            true),
    USER_WITHDRAWAL("사용자 매장 탈퇴"
            ,"회원님이 관리중인 %1$s 매장에 대한 접근 권한을 포기한 사용자가 있습니다.",
            "사용자 %2$s(이)가 %1$s 매장에 대한 접근 권한을 포기했습니다.",
            true
            )
    ;

    private String desc;
    private String titleTemplate;
    private String contentTemplate;
    private Boolean toAdmin;

    public String getTitleFrom(String... strings){
        return String.format(this.getTitleTemplate(), (Object[])strings);
    }
    public String getContentFrom(String... strings){

        return String.format(this.getContentTemplate(), (Object[])strings);
    }

    static public List<AccessNotificationType> findBy(final AccessSituationType type){
        // ACCESSIBLE, INACCESSIBLE, ADMIN, WAITING
        switch (type){
            case APPLIED:
                return List.of(ACCESS_APPLY, APPLLICATION_CAME);
            case APPLY_CANCELED:
                return List.of(APPLICATION_CANCELD);
            case WITHDRAWAL:
                return List.of(USER_WITHDRAWAL);
            case ACCESS_APPRVOED:
                return List.of(ACCESS_APPROVED);
            case ACCESS_DENIED:
                return List.of(ACCESS_DENIED);
            case ADMIN_PROMOTED:
                return List.of(ACCESS_ADMIN_PROMOTION);
            case ACCESS_REMOVED:
                return List.of(ACCESS_DELETED);
        }
        return null;
    }


}
