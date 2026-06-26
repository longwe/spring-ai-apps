package com.ezcloud.basicchat.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
class ChatController {

    private final ChatClient openAiClient;
    private final ChatClient anthropicClient;
    private final ChatClient googleClient;

    ChatController(
            @Qualifier("openAiChatClient")   ChatClient openAiClient,
            @Qualifier("anthropicChatClient") ChatClient anthropicClient,
            @Qualifier("googleChatClient")    ChatClient googleClient) {
        this.openAiClient   = openAiClient;
        this.anthropicClient = anthropicClient;
        this.googleClient    = googleClient;
    }

    @GetMapping
    String chat(@RequestParam String message,
                @RequestParam(defaultValue = "openai") String model) {
        return resolve(model).prompt().user(message).call().content();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> stream(@RequestParam String message,
                        @RequestParam(defaultValue = "openai") String model) {
        return resolve(model).prompt().user(message).stream().content();
    }

    private ChatClient resolve(String model) {
        return switch (model.toLowerCase()) {
            case "anthropic" -> anthropicClient;
            case "google", "gemini" -> googleClient;
            default -> openAiClient;
        };
    }
}
