package com.stockguard.stockguard.dto.response;

import lombok.Data;

import java.util.List;

public class AIResponse {

    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;
    }

    @Data
    public static class Message {
        private String content;
    }

    public String getContent() {
        if (choices != null && !choices.isEmpty()) {
            return choices.get(0).getMessage().getContent();
        }
        return null;
    }
}
