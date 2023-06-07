package com.dambae200.dambae200.domain.cigaretteOnList.controller;

import com.dambae200.dambae200.domain.cigaretteOnList.dto.*;
import com.dambae200.dambae200.domain.cigaretteOnList.service.CigaretteOnListUpdateService;
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.socket.dto.SocketMessage;
import com.dambae200.dambae200.global.socket.dto.SocketRequest;
import com.dambae200.dambae200.global.common.dto.StandardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@Controller
@Slf4j
public class CigaretteOnListStompController {

    private final SimpMessagingTemplate template; // 특정 Broker로 메시지를 전달
    private final CigaretteOnListUpdateService cigaretteOnListUpdateService;

    @MessageMapping("/socket/store/cigar_num") // -> pub/store/cigar_num
    public void cigarNum(@Payload final SocketRequest<CigaretteOnListUpdateCountRequest> request){
        final CigaretteOnListUpdateCountRequest messageContent = request.getContent();
        String destination = "/sub/store/" + messageContent.getStoreId() + "/cigar_num";

        final CigaretteOnListUpdateCountResponse response = cigaretteOnListUpdateService.inputCigaretteCount(messageContent.getStoreId(), messageContent.getCigarOnListId(), messageContent.getNum());
        final StandardResponse<SocketMessage<CigaretteOnListUpdateCountResponse>> message = StandardResponse.ok(new SocketMessage<>(request.getRequestUserId(), response));
        template.convertAndSend(destination, message);
    }

    @MessageMapping("/socket/store/add_cigar")
    public void addCigaretteOnListById(Principal principal, @Payload @Valid final SocketRequest<CigaretteOnListAddRequest> request) {
        final String destination = "/sub/store/" + request.getContent().getStoreId() + "/add_cigar";
        final CigaretteOnListGetResponse response = cigaretteOnListUpdateService.addCigaretteOnList(request.getContent());
        final StandardResponse<SocketMessage<CigaretteOnListGetResponse>> message = StandardResponse.ok(new SocketMessage<>(request.getRequestUserId(), response));
        template.convertAndSend(destination, message);
    }

    @MessageMapping("/socket/store/initialize_count")
    public void initializeCigaretteCount(@Payload final SocketRequest<CigaretteOnListInitializeCountRequest> request) {
        final CigaretteOnListInitializeCountRequest requestContent = request.getContent();
        cigaretteOnListUpdateService.initializeCigaretteCount(requestContent.getStoreId());

        final String destination = "/sub/store/" + requestContent.getStoreId() + "/initialize_count";
        final StandardResponse<SocketMessage<Object>> message = StandardResponse.ok(new SocketMessage<>(request.getRequestUserId(), null));
        template.convertAndSend(destination, message);
    }

    @MessageMapping("/socket/store/delete_cigar")
    public void deleteCigaretteOnList(@Payload final SocketRequest<CigaretteOnListDeleteRequest> request) {
        final CigaretteOnListDeleteRequest requestContent = request.getContent();
        final DeleteResponse response = cigaretteOnListUpdateService.deleteCigaretteOnList(requestContent.getStoreId(), requestContent.getId());

        final String destination = "/sub/store/" + requestContent.getStoreId() + "/delete_cigar";
        final StandardResponse<SocketMessage<DeleteResponse>> message = StandardResponse.ok(new SocketMessage<>(request.getRequestUserId(), response));
        template.convertAndSend(destination, message);
    }

    @MessageMapping("/socket/store/modify_cigar")
    public void modifyCigaretteOnList(@Payload final SocketRequest<CigaretteOnListModifyRequest> request) {
        final CigaretteOnListModifyRequest requestContent = request.getContent();
        final CigaretteOnListModifyResponse response = cigaretteOnListUpdateService.modifyCustomizeName(requestContent.getStoreId(), requestContent.getId(), requestContent.getCustomizedName());

        final String destination = "/sub/store/" + requestContent.getStoreId() + "/modify_cigar";
        final StandardResponse<SocketMessage<CigaretteOnListModifyResponse>> message = StandardResponse.ok(new SocketMessage<>(request.getRequestUserId(), response));
        template.convertAndSend(destination, message);
    }

    @MessageMapping("/socket/store/reorder")
    public void reorder(@Payload final SocketRequest<CigaretteOnListReorderRequest> request) {
        final CigaretteOnListReorderRequest requestContent = request.getContent();
        final CigaretteOnListReorderResponse response = cigaretteOnListUpdateService.reOrderAll(requestContent);

        final String destination = "/sub/store/" + requestContent.getStoreId() + "/reorder";
        final StandardResponse<SocketMessage<CigaretteOnListReorderResponse>> message = StandardResponse.ok(new SocketMessage<>(request.getRequestUserId(), response));
        template.convertAndSend(destination, message);
    }

}
