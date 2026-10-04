package com.altees.OpenAIExample.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatClient chatClient;


    public ChatController(@Qualifier("memoryChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping
    public String chat(@RequestParam("message") String message) {
        return chatClient.prompt(message).call().content();
    }

    @GetMapping("/dev")
    public String chatForDevloper(@RequestParam("message") String message) {
        return chatClient.prompt(message)
                .system("You are A Java full stack Software architect,you role is" +
                        "to provide assistance to the fresher or junior developer" +
                        "with programming and other related question,Dont accept any question" +
                        "related to other non programming topic,Related To java also dont take or assist with any question" +
                        "which asked above java 1.8 version,Keep the assistant message short and concise")
                .user(message)
                .call().content();
    }
}
