package com.dambae200.dambae200.global.common;

import lombok.Getter;

import java.util.List;

@Getter
public class DeleteResponse {
    String message;

    public DeleteResponse(String entityName, List<Long> idList){
        this.message = idList.size() + " 개의 " + entityName + " 삭제 완료";
    }

    public DeleteResponse(String entityName, Long id){
        this.message = new DeleteResponse(entityName, List.of(id)).getMessage();
    }
}