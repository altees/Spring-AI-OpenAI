package com.altees.OpenAIExample.controller;

import com.altees.OpenAIExample.model.CityInfo;
import com.altees.OpenAIExample.model.CityResponseRecord;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/str")
public class StructuredChatResponseController {

    private final ChatClient chatClient;

    public StructuredChatResponseController(ChatClient.Builder chatClientBuilder) {
        OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                .model("gpt-4.1-mini")
                .temperature(0.9)
                .build();
        this.chatClient = chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultOptions(chatOptions.mutate())
                .build();
    }


    @GetMapping()
    public CityResponseRecord hello(@RequestParam("msg") String msg) {
        CityResponseRecord entity = chatClient.prompt()
                .user(msg)
                .call().entity(CityResponseRecord.class);
        return entity;
    }
    @GetMapping("/list")
    public List<CityInfo> list(@RequestParam("msg") String msg) {
        List<CityInfo> entity = chatClient.prompt()
                .user(msg)
                .call().entity(new ParameterizedTypeReference<List<CityInfo>>() {});
        return entity;
    }
    @GetMapping("/map")
    public Map<String, Object> map(@RequestParam("msg") String msg) {
        Map<String, Object> entity = chatClient.prompt()
                .user(msg)
                .call().entity(new MapOutputConverter());
        return entity;
    }
}
