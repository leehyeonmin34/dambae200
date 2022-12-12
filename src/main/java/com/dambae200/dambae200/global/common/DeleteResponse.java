package com.dambae200.dambae200.global.common;

import lombok.Getter;

import java.util.List;

@Getter
public class DeleteResponse {
    String message;
    List<Long> idList;

    public DeleteResponse(String entityName, List<Long> idList){
        this.message = getMessage(entityName, idList);
        this.idList = idList;
    }

    public DeleteResponse(String entityName, Long id){
        this.message = getMessage(entityName, id);
        this.idList = List.of(id);
    }

    private String getMessage(String entityName, List<Long> idList){
        return idList.size() + " 개의 " + entityName + " 삭제 완료";
    }

    private String getMessage(String entityName, Long id){
        return getMessage(entityName, List.of(id));
    }

}