package com.ezcloud.basicchat.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
 public class SimpleAiController {

 private final ChatClient chatClient;

 public SimpleAiController(ChatClient.Builder builder) {
  this.chatClient = builder.build();
 }

 @GetMapping("/ai/basic")
 public Map<String, String> generation(
         @RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
  return Map.of("generation", Objects.requireNonNull(chatClient.prompt().user(message).call().content()));
 }
}
