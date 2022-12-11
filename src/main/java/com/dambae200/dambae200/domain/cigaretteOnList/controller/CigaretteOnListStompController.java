package com.dambae200.dambae200.domain.cigaretteOnList.controller;

import com.dambae200.dambae200.domain.access.service.AccessService;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.*;
import com.dambae200.dambae200.domain.cigaretteOnList.service.CigaretteOnListUpdateService;
import com.dambae200.dambae200.global.common.DeleteResponse;
import com.dambae200.dambae200.global.common.SocketMessage;
import com.dambae200.dambae200.global.common.SocketRequest;
import com.dambae200.dambae200.global.common.StandardResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class CigaretteOnListStompController {

    private final SimpMessagingTemplate template; // 특정 Broker로 메시지를 전달
    private final CigaretteOnListUpdateService cigaretteOnListUpdateService;
    private final AccessService accessService;
    private final ObjectMapper objectMapper;

    @MessageMapping("/store/cigar_num") // -> pub/store/cigar_num
    public void cigarNum(@Payload SocketRequest<CigaretteOnListUpdateCountRequest> request){
        accessService.checkAccess(request.getRequestUserId(), request.getContent().getStoreId());

        CigaretteOnListUpdateCountRequest messageContent = request.getContent();
        String destination = "/sub/store/" + messageContent.getStoreId() + "/cigar_num";

        CigaretteOnListGetResponse response = cigaretteOnListUpdateService.inputCigaretteCount(messageContent.getStoreId(), messageContent.getCigarOnListId(), messageContent.getNum());
        StandardResponse<SocketMessage<CigaretteOnListGetResponse>> message = StandardResponse.ok(new SocketMessage<>(request.getRequestUserId(), response));
        template.convertAndSend(destination, message);
    }

    @MessageMapping("/store/add_cigar")
    public void addCigaretteOnListById(Principal principal, @Payload @Valid SocketRequest<CigaretteOnListAddRequest> request) {
        accessService.checkAccess(request.getRequestUserId(), request.getContent().getStoreId());

        String destination = "/sub/store/" + request.getContent().getStoreId() + "/add_cigar";
        CigaretteOnListGetResponse response = cigaretteOnListUpdateService.addCigaretteOnListById(request.getContent());
        StandardResponse<SocketMessage<CigaretteOnListGetResponse>> message = StandardResponse.ok(new SocketMessage<>(request.getRequestUserId(), response));
        template.convertAndSend(destination, message);
    }

    @MessageMapping("/store/initialize_count")
    public void initializeCigaretteCount(@Payload SocketRequest<CigaretteOnListInitializeCountRequest> request) {
        accessService.checkAccess(request.getRequestUserId(), request.getContent().getStoreId());

        CigaretteOnListInitializeCountRequest requestContent = request.getContent();
        CigaretteOnListGetListResponse response = cigaretteOnListUpdateService.initializeCigaretteCount(requestContent.getStoreId());

        String destination = "/sub/store/" + requestContent.getStoreId() + "/initialize_count";
        StandardResponse<SocketMessage<CigaretteOnListGetListResponse>> message = StandardResponse.ok(new SocketMessage<>(request.getRequestUserId(), response));
        template.convertAndSend(destination, message);
    }

    @MessageMapping("/store/delete_cigar")
    public void deleteCigaretteOnList(@Payload SocketRequest<CigaretteOnListDeleteRequest> request) {
        accessService.checkAccess(request.getRequestUserId(), request.getContent().getStoreId());

        CigaretteOnListDeleteRequest requestContent = request.getContent();
        DeleteResponse response = cigaretteOnListUpdateService.deleteCigaretteOnList(requestContent.getId());

        String destination = "/sub/store/" + requestContent.getStoreId() + "/delete_cigar";
        StandardResponse<SocketMessage<DeleteResponse>> message = StandardResponse.ok(new SocketMessage<>(request.getRequestUserId(), response));
        template.convertAndSend(destination, message);
    }

    @MessageMapping("/store/modify_cigar")
    public void modifyCigaretteOnList(@Payload SocketRequest<CigaretteOnListModifyRequest> request) {
        accessService.checkAccess(request.getRequestUserId(), request.getContent().getStoreId());

        CigaretteOnListModifyRequest requestContent = request.getContent();
        CigaretteOnListGetResponse response = cigaretteOnListUpdateService.modifyCustomizeName(requestContent.getId(), requestContent.getCustomizedName());

        String destination = "/sub/store/" + requestContent.getStoreId() + "/modify_cigar";
        StandardResponse<SocketMessage<CigaretteOnListGetResponse>> message = StandardResponse.ok(new SocketMessage<>(request.getRequestUserId(), response));
        template.convertAndSend(destination, message);
    }

    @MessageMapping("/store/reorder")
    public void reorder(@Payload SocketRequest<CigaretteOnListReorderRequest> request) {
        accessService.checkAccess(request.getRequestUserId(), request.getContent().getStoreId());

        CigaretteOnListReorderRequest requestContent = request.getContent();
        CigaretteOnListReorderResponse response = cigaretteOnListUpdateService.reOrderAll(requestContent);

        String destination = "/sub/store/" + requestContent.getStoreId() + "/reorder";
        StandardResponse<SocketMessage<CigaretteOnListReorderResponse>> message = StandardResponse.ok(new SocketMessage<>(request.getRequestUserId(), response));
        template.convertAndSend(destination, message);
    }



}
