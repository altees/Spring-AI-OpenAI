package com.altees.OpenAIExample.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/rag")
@RestController
public class RagController {

    @Autowired
    @Qualifier("memoryChatClient")
    private ChatClient chatClient;

    @GetMapping("/chat")
    public String chat(@RequestHeader("x-usr-id") String userId, @RequestParam("msg") String msg) {
        return chatClient.prompt()
                .user(msg)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, userId))
                .call()
                .content();
    }
}
