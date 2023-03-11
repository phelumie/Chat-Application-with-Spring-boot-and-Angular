package com.ajisegiri.chatapp.controller;

import com.ajisegiri.chatapp.dto.Message;
import com.ajisegiri.chatapp.model.Chat;
import com.ajisegiri.chatapp.repo.ChatRepository;
import com.ajisegiri.chatapp.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.ajisegiri.chatapp.model.Chat.MESSAGE_SEQUENCE;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final ChatRepository chatRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @SneakyThrows
    @MessageMapping("/chat/{to}")
    public void sendMessage(@DestinationVariable String to , Message message) {
        System.out.println("handling send message: " + message + " to: " + to);
        // to reduce latency
        CompletableFuture.runAsync(() -> {
            var chat=Chat.builder().id(sequenceGeneratorService.getSequenceNumber(MESSAGE_SEQUENCE).toString())
                    .to(message.to()).channel(to).from(message.from())
                    .content(message.content().replaceAll("\\s"," ")).timestamp(new Date()).build();
            chatRepository.save(chat);
        }).exceptionally(ex->{
            log.error("Error persisting data: " + ex.getMessage());
            return null;
        });

        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);

    }


}
