package com.altees.OpenAIExample.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;

@Slf4j
public class TokenUsageAuditAdvisor implements CallAdvisor {
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        logTokenUsage(chatClientResponse);
        return chatClientResponse;
    }

    private void logTokenUsage(ChatClientResponse chatClientResponse) {
        if (chatClientResponse.chatResponse() == null || chatClientResponse.chatResponse().getMetadata() == null ||
                chatClientResponse.chatResponse().getMetadata().getUsage() == null) {
            log.info("No token usage information available in the response metadata or any of the object from response is null.");
            return;
        }
        Usage usage = chatClientResponse.chatResponse().getMetadata().getUsage();
        log.info("Token usage details : {}", usage);
    }

    @Override
    public String getName() {
        return "TokenUsageAuditAdvisor";
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
