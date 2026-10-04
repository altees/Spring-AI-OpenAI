package com.altees.OpenAIExample.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doc")
public class RagDocumentController {


    @Autowired
    private VectorStore vectorStore;

    @Autowired()
    @Qualifier("memoryChatClient")
    private ChatClient chatClient;


    @GetMapping("/pdf/load")
    public String loadPdf() {

        TikaDocumentReader reader =
                new TikaDocumentReader(
                        new ClassPathResource("Leave Policy.pdf")
                );


        List<Document> documents = reader.get();

        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(100)
                .withMaxNumChunks(400)
                .build();
        List<Document> splittedDocs = splitter.apply(documents);
        vectorStore.add(splittedDocs);
        return "PDF loaded and processed successfully!";
    }


    @Value("classpath:/prompt_template/ragSystemMsgTemplate.st")
    private Resource resource;


    @GetMapping("/chat")
    public String chat(@RequestHeader("x-usr-id") String userId, @RequestParam("msg") String msg) {

        return chatClient.prompt()
                .user(msg)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, userId))
                .call()
                .content();

    }


}
