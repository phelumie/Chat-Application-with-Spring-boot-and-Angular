package com.ajisegiri.chatapp.events;

import org.json.JSONObject;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public final class EventBusService {
    private static Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    public static final  String ACTIVE_USERS_EVENT="active-users";
    private static SimpUserRegistry simpUserRegistry;

    public EventBusService(SimpUserRegistry simpUserRegistry) {
        this.simpUserRegistry = simpUserRegistry;
    }

    public synchronized static Map<String, SseEmitter> getEmitters() {
        return emitters;
    }

    public static void emitData(String username,String operation) {
        String eventData=new JSONObject()
                .put("username",username)
                .put("operation",operation).toString();
        getEmitters().entrySet().stream().forEach(emitterEntry->{
            try {
                emitterEntry.getValue().send(SseEmitter.event().name(ACTIVE_USERS_EVENT).data(eventData));
            } catch (IOException e) {
                getEmitters().remove(emitterEntry.getKey());
            }
        });


    }
}
