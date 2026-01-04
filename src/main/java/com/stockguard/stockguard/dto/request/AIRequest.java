package com.stockguard.stockguard.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRequest {

    private String model;
    private List<Message> messages;
    private double temperature;

    @Data
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }

    public static AIRequest create(String model, String userPrompt) {
        List<Message> msgs = new ArrayList<>();
        msgs.add(new Message("user", userPrompt));
        return new AIRequest(model, msgs, 0.7);
    }
}
