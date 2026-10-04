package com.altees.OpenAIExample.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.Generation;

import java.util.List;

public class RegardsResponseAdvisor implements CallAdvisor {
    @Override
    public ChatClientResponse adviseCall(
            ChatClientRequest request,
            CallAdvisorChain chain) {

        // Execute next advisor/model
        ChatClientResponse response = chain.nextCall(request);

        // Get original response text
        AssistantMessage originalMessage =
                response.chatResponse()
                        .getResult()
                        .getOutput();

        String content = originalMessage.getText();

        // Modify response
        AssistantMessage updatedMessage =
                new AssistantMessage(
                        content +
                                "\n\nYe AI generated response hai bhai, tu dekh le apne hisab se."
                );

        // Create updated generation
        Generation generation = new Generation(updatedMessage);

        // Build updated response
        return ChatClientResponse.builder()
                .chatResponse(
                        response.chatResponse()
                                .builder()
                                .generations(List.of(generation))
                                .build()
                )
                .context(response.context())
                .build();
    }

    @Override
    public String getName() {
        return "RegardsResponseAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
