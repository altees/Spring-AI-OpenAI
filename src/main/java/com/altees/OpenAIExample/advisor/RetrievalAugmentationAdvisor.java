package com.altees.OpenAIExample.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class RetrievalAugmentationAdvisor implements CallAdvisor {

    private final VectorStore vectorStore;
    private final Resource systemPromptTemplate;
    private final int topK;
    private final double similarityThreshold;

    public RetrievalAugmentationAdvisor(VectorStore vectorStore, Resource systemPromptTemplate) {
        this(vectorStore, systemPromptTemplate, 2, 0.9);
    }

    public RetrievalAugmentationAdvisor(VectorStore vectorStore, Resource systemPromptTemplate,
                                         int topK, double similarityThreshold) {
        this.vectorStore = vectorStore;
        this.systemPromptTemplate = systemPromptTemplate;
        this.topK = topK;
        this.similarityThreshold = similarityThreshold;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        ChatClientRequest augmentedRequest = augmentWithContext(request);
        return chain.nextCall(augmentedRequest);
    }

    private ChatClientRequest augmentWithContext(ChatClientRequest request) {
        String userQuery = extractUserQuery(request);
        if (userQuery == null || userQuery.isBlank()) {
            return request;
        }

        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userQuery)
                        .topK(topK)
                        .similarityThreshold(similarityThreshold)
                        .build()
        );

        log.info("RAG retrieved {} documents for query: {}", docs.size(), userQuery);

        String context = docs.stream()
                .map(Document::getText)
                .collect(Collectors.joining(". "));

        String systemText = loadTemplate().replace("{document}", context);

        Prompt originalPrompt = request.prompt();
        List<org.springframework.ai.chat.messages.Message> messages = new ArrayList<>(originalPrompt.getInstructions());

        // Replace or prepend system message with RAG context injected
        messages.removeIf(m -> m instanceof SystemMessage);
        messages.add(0, new SystemMessage(systemText));

        Prompt augmentedPrompt = new Prompt(messages, originalPrompt.getOptions());
        return ChatClientRequest.builder()
                .prompt(augmentedPrompt)
                .context(request.context())
                .build();
    }

    private String extractUserQuery(ChatClientRequest request) {
        return request.prompt().getInstructions().stream()
                .filter(m -> m instanceof org.springframework.ai.chat.messages.UserMessage)
                .map(org.springframework.ai.chat.messages.Message::getText)
                .findFirst()
                .orElse(null);
    }

    private String loadTemplate() {
        try {
            return systemPromptTemplate.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to load RAG system prompt template", e);
            return "Use the following context to answer: {document}";
        }
    }

    @Override
    public String getName() {
        return "RetrievalAugmentationAdvisor";
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
