package com.ezcloud.chatwithmemory.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final ChatMemory chatMemory;
    private final MessageChatMemoryAdvisor memoryAdvisor;

    ChatController(
            @Qualifier("openAiChatClient")    ChatClient openAiClient,
            @Qualifier("anthropicChatClient") ChatClient anthropicClient,
            @Qualifier("googleChatClient")    ChatClient googleClient,
            ChatMemory chatMemory) {
        this.openAiClient    = openAiClient;
        this.anthropicClient = anthropicClient;
        this.googleClient    = googleClient;
        this.chatMemory      = chatMemory;
        this.memoryAdvisor   = MessageChatMemoryAdvisor.builder(chatMemory).build();
    }

    @GetMapping
    String chat(@RequestParam String message,
                @RequestParam(defaultValue = "openai") String model,
                @RequestParam(defaultValue = "default") String conversationId) {
        return resolve(model).prompt()
                .user(message)
                .advisors(spec -> spec
                        .advisors(memoryAdvisor)
                        .param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> stream(@RequestParam String message,
                        @RequestParam(defaultValue = "openai") String model,
                        @RequestParam(defaultValue = "default") String conversationId) {
        return resolve(model).prompt()
                .user(message)
                .advisors(spec -> spec
                        .advisors(memoryAdvisor)
                        .param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    @DeleteMapping("/memory/{conversationId}")
    void clearMemory(@PathVariable String conversationId) {
        chatMemory.clear(conversationId);
    }

    private ChatClient resolve(String model) {
        return switch (model.toLowerCase()) {
            case "anthropic" -> anthropicClient;
            case "google", "gemini" -> googleClient;
            default -> openAiClient;
        };
    }
}
