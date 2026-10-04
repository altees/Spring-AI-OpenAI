package com.altees.OpenAIExample.controller;


import com.altees.OpenAIExample.service.EmployeeService;
import com.altees.OpenAIExample.tools.DateTimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tools")
public class ToolsController {

    @Autowired
    @Qualifier("toolChatClient")
    private ChatClient chatClient;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DateTimeTools dateTimeTools;


    @GetMapping("/datetime")
    public String getCurrentDateTime(@RequestParam String msg, @RequestHeader("x-usr-id") String username) {
        String userMessage = "What is the current date and time?";
        return chatClient.prompt()
                .user(msg)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, username))
                .tools(dateTimeTools)
                .call()
                .content();
    }
    @GetMapping("/emp-details")
    public String getCurrentDateTime(@RequestParam String msg, @RequestHeader("x-usr-id") long empId) {
        String userMessage = "What is the current date and time?";
        return chatClient.prompt()
                .user(msg)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, empId))
                .tools(employeeService)
                .call()
                .content();
    }

}
