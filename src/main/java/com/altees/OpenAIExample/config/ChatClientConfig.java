package com.altees.OpenAIExample.config;

import com.altees.OpenAIExample.advisor.RegardsResponseAdvisor;
import com.altees.OpenAIExample.advisor.TokenUsageAuditAdvisor;
import com.altees.OpenAIExample.tools.DateTimeTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class ChatClientConfig {

    private static final String SYSTEM_MSG = """
            You are an e-commerce support assistant. Help only with orders, shipping, payments, products,
             and related support issues. Politely decline unrelated questions.
             Keep responses short, clear, and to the point.""";

    @Bean(name = "memoryChatClient1")
    public ChatClient chatClient(ChatClient.Builder cahtclientBuilder) {


        OpenAiChatOptions co = OpenAiChatOptions.builder()  // ✅ OpenAI-specific options
                .model("gpt-4.1-mini")  // or your preferred model like "gpt-4-turbo", "gpt-3.5-turbo"
                .temperature(0.9)
                .maxTokens(40)
                .build();

        return cahtclientBuilder.defaultSystem(SYSTEM_MSG)
                .defaultOptions(co.mutate())
                .defaultAdvisors(List.of(new TokenUsageAuditAdvisor(), new SimpleLoggerAdvisor(0), new RegardsResponseAdvisor()))
                .build();
    }

    @Bean(name = "memoryChatClient")
    public ChatClient memoryChatClient(ChatClient.Builder chatclientBuilder, ChatMemory chatMemory, VectorStore vectorStore,
                                       RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {
        Advisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory)
                .build();
        SimpleLoggerAdvisor loggerAdvisor = new SimpleLoggerAdvisor();


        OpenAiChatOptions co = OpenAiChatOptions.builder()
                .model("gpt-4.1-mini")
                .temperature(0.9)
                .build();

        return chatclientBuilder
                .defaultOptions(co.mutate())
                .defaultAdvisors(List.of(loggerAdvisor, chatMemoryAdvisor, retrievalAugmentationAdvisor))
                .defaultAdvisors(spec -> spec.param(
                        ChatMemory.CONVERSATION_ID,
                        "global-chat"
                ))
                .build();
    }

    @Bean(name = "toolChatClient")
    public ChatClient toolChatClient(ChatClient.Builder chatclientBuilder, ChatMemory chatMemory) {
        Advisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory)
                .build();
        SimpleLoggerAdvisor loggerAdvisor = new SimpleLoggerAdvisor();
        OpenAiChatOptions co = OpenAiChatOptions.builder()
                .model("gpt-4.1-mini")
                .temperature(0.9)
                .build();

        return chatclientBuilder
                .defaultOptions(co.mutate())
                .defaultAdvisors(List.of(loggerAdvisor, chatMemoryAdvisor))
                .defaultTools(new DateTimeTools()) // Registering the DateTimeTools as a tool
                .defaultAdvisors(spec -> spec.param(
                        ChatMemory.CONVERSATION_ID,
                        "global-chat"
                ))
                .build();
    }


    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .maxMessages(5)
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
    }


    @Bean
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore,
                                                              ChatClient.Builder chatClientBuilder) {
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .topK(3)
                        .similarityThreshold(0.5)
                        .build())
                .build();
    }
}
