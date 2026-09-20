package com.consultationapp.demo.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
public class SignalingController {

    private final SimpMessagingTemplate messagingTemplate;

    private final Map<String, Set<String>> rooms =
            new ConcurrentHashMap<>();

    @MessageMapping("/join")
    public void join(SignalMessage message) {

        String roomId = message.getRoomId();
        String senderId = message.getSenderId();

        Set<String> participants =
                rooms.computeIfAbsent(
                        roomId,
                        key -> ConcurrentHashMap.newKeySet()
                );

        boolean peerAlreadyExists =
                !participants.isEmpty()
                        && !participants.contains(senderId);

        participants.add(senderId);

        System.out.println(
                "JOIN room=" + roomId
                        + " sender=" + senderId
                        + " peerAlreadyExists=" + peerAlreadyExists
        );

        if (peerAlreadyExists) {

            SignalMessage peerPresent =
                    SignalMessage.builder()
                            .type("PEER_PRESENT")
                            .roomId(roomId)
                            .senderId(
                                    participants.stream()
                                            .filter(id -> !id.equals(senderId))
                                            .findFirst()
                                            .orElse("")
                            )
                            .build();

            messagingTemplate.convertAndSend(
                    "/topic/room/" + roomId,
                    peerPresent
            );
        }

        SignalMessage peerJoined =
                SignalMessage.builder()
                        .type("PEER_JOINED")
                        .roomId(roomId)
                        .senderId(senderId)
                        .build();

        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId,
                peerJoined
        );
    }

    @MessageMapping("/signal")
    public void signal(SignalMessage message) {

        System.out.println(
                "SIGNAL type=" + message.getType()
                        + " room=" + message.getRoomId()
                        + " sender=" + message.getSenderId()
        );

        messagingTemplate.convertAndSend(
                "/topic/room/" + message.getRoomId(),
                message
        );
    }

    @MessageMapping("/leave")
    public void leave(SignalMessage message) {

        String roomId = message.getRoomId();
        String senderId = message.getSenderId();

        Set<String> participants = rooms.get(roomId);

        if (participants != null) {
            participants.remove(senderId);

            if (participants.isEmpty()) {
                rooms.remove(roomId);
            }
        }

        System.out.println(
                "LEAVE room=" + roomId
                        + " sender=" + senderId
        );

        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId,
                message
        );
    }
}