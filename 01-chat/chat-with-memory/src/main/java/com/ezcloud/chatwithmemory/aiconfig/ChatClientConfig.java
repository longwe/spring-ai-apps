package com.ezcloud.chatwithmemory.aiconfig;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build();
    }

    @Bean
    @Primary
    public ChatClient.Builder chatClientBuilder(OpenAiChatModel model) {
        return ChatClient.builder(model);
    }

    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel model) {
        return ChatClient.builder(model).build();
    }

    @Bean
    public ChatClient anthropicChatClient(AnthropicChatModel model) {
        return ChatClient.builder(model).build();
    }

    @Bean
    public ChatClient googleChatClient(GoogleGenAiChatModel model) {
        return ChatClient.builder(model).build();
    }
}
