package com.altees.OpenAIExample.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/prompt")
@Slf4j
public class PromptingTemplateController {

    @Autowired
    @Qualifier("memoryChatClient")
    private ChatClient chatClient;


    @Value("classpath:/prompt_template/email_prompt_template.st")
    Resource emailTemplate;


    @GetMapping("/email")
    public String emailResponse(@RequestParam("name") String name, @RequestParam("message") String message) {
        return chatClient.prompt()
                .user(emailTemplateResource -> emailTemplateResource.text(emailTemplate)
                        .params(new HashMap<>() {{
                            put("name", name);
                            put("message", message);
                        }}))
                .call().content();


    }


}
