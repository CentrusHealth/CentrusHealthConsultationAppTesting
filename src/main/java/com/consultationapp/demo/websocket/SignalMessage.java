package com.consultationapp.demo.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignalMessage {

    private String type;

    private String roomId;

    private String senderId;

    private String data;
}