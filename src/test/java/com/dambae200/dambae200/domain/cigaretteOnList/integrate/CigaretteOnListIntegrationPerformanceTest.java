package com.dambae200.dambae200.domain.cigaretteOnList.integrate;

import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListGetResponse;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListUpdateCountRequest;
import com.dambae200.dambae200.domain.cigaretteOnList.integrate.builder.CigaretteOnListUpdateCountRequestBuilder;
import com.dambae200.dambae200.domain.cigaretteOnList.integrate.builder.SocketRequestBuilder;
import com.dambae200.dambae200.global.common.dto.StandardResponse;
import com.dambae200.dambae200.global.socket.dto.SocketMessage;
import com.dambae200.dambae200.global.socket.dto.SocketRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.MatcherAssert.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(classes = Dambae200Application.class)
@AutoConfigureMockMvc
@Transactional
public class CigaretteOnListIntegrationPerformanceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    protected StompSession stompSession;

    @LocalServerPort
    private int port;

    private final String url;

    private final String ENDPOINT = "/stomp/store";

    private final WebSocketStompClient websocketClient;

    public CigaretteOnListIntegrationPerformanceTest() {
        this.websocketClient = new WebSocketStompClient(new SockJsClient(createTransport()));
        this.websocketClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.url = "ws://localhost:";
    }

    @BeforeEach
    public void connect() throws ExecutionException, InterruptedException, TimeoutException {
        this.stompSession = this.websocketClient
                .connect(url + port + ENDPOINT, new StompSessionHandlerAdapter() {})
                .get(3, TimeUnit.SECONDS);
    }

    @AfterEach
    public void disconnect() {
        if (this.stompSession.isConnected()) {
            this.stompSession.disconnect();
        }
    }

    private List<Transport> createTransport() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    @Test
    @Transactional
    public void CigaretteOnListIntegrationPerformanceTest_Optimized() throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        // GIVEN
        String storeId = "1089";
        MessageFrameHandler<StandardResponse> handler = new MessageFrameHandler<>(StandardResponse.class);
        this.stompSession.subscribe("/sub/store/" + storeId + "/cigar_num", handler);
        this.stompSession.subscribe("/user/sub/store/" + storeId + "/cigar_num", handler);
        CigaretteOnListUpdateCountRequest requestContent = CigaretteOnListUpdateCountRequestBuilder.build();
        SocketRequest<CigaretteOnListUpdateCountRequest> socketRequest = SocketRequestBuilder.build(requestContent);
        StompHeaders headers = new StompHeaders();
        headers.add("Authorization", "e4dc092f-68de-419c-b6fb-cdb6eb66a663");
        headers.add("Content-Type", "application/json");
        headers.add("destination", "/pub/store/cigar_num");

        // WHEN
        this.stompSession.send(headers, socketRequest);

        /* THEN */
        StandardResponse result = handler.getCompletableFuture().get(10, TimeUnit.SECONDS);

        then(result).isNotNull();
        System.out.println(result);
    }


}
