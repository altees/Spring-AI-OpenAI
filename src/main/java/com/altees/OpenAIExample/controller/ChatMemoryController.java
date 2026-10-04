package com.altees.OpenAIExample.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatMemoryController {

    @Autowired
    @Qualifier("memoryChatClient")
    ChatClient memoryChatClient;

    @GetMapping("/mem-chat")
    public String memoryChat(@RequestParam String msg,
                             @RequestHeader("x-usr-id") String id) {
        return memoryChatClient.prompt()
                .user(msg)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, id))
                .call()
                .content();
    }
}
