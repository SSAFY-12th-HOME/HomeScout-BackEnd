package com.ssafy.homescout.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //메시지 브로커 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    //WebSocket 엔드포인트를 정의
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat") //클라이언트가 WebSocket 연결을 시작하는 특정 URL 경로
                .withSockJS(); //SockJS를 활성화하여 WebSocket 연결이 불가능한 환경에서도 클라이언트와 서버 간의 실시간 통신을 가능하게 함
    }

    //MessageBrokerRegistry : Spring의 WebSocket 설정에서 메시지 브로커를 구성하기 위한 API를 제공
    //메시지 브로커의 경로(prefix)와 유형을 설정
    //클라이언트와 서버 간의 메시지 라우팅 방식을 정의
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        //Spring에서 제공하는 간단한 메시지 브로커(SimpleBroker)를 활성화
        //클라이언트가 구독할 수 있는 목적지(destination) 경로를 지정 -> 메시지 브로커로 간다.
        //queue : Point-to-Point (1:1) 메시징
        //topic : pub/sub 메시징 -> 이후 채팅 기능 확장성 위해 추가해놓은 것
        config.enableSimpleBroker("/queue", "/topic"); //메시지 브로커 경로 설정


        //setApplicationDestinationPrefixes : 클라이언트가 메시지를 보낼 때 사용할 목적지(prefix)를 지정 -> 컨트롤러로 간다.
        //클라이언트는 이 prefix(어디로 가야하는지 구분하는 역할을 함 컨트롤러로 갈지, 메시지 브로커로 갈지)가 붙은 경로로 메시지를 보낸다.
        //서버 측에서는 이 경로에 매핑된 컨트롤러 메서드가 메시지를 처리한다.
        config.setApplicationDestinationPrefixes("/app"); // 애플리케이션 경로 설정

        // 사용자 대상 메시지 전송을 위한 prefix 설정
        config.setUserDestinationPrefix("/user");
    }
}
