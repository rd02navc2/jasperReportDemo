package com.beyond.surrounding.talk.dto;

import lombok.Data;
import java.util.List;

@Data
public class OpenAIResponse {
    private List<Choice> choices;
    private ErrorInfo error;

    @Data
    public static class Choice {
        private Message message;
    }

    @Data
    public static class Message {
        private String content;
    }

    @Data
    public static class ErrorInfo {
        private String message;
    }
}