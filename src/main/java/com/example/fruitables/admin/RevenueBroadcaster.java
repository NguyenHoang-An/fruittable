package com.example.fruitables.admin;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class RevenueBroadcaster {
    public final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L); // Không timeout
        this.emitters.add(emitter);

        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));
        emitter.onError((e) -> this.emitters.remove(emitter));

        return emitter;
    }

    public void push(BigDecimal totalRevenueToday) {
        for(SseEmitter emitter : emitters) {
            try{
                emitter.send(SseEmitter.event()
                        .name("rênvue")
                        .id(String.valueOf(Instant.now().toEpochMilli()))
                        .data(totalRevenueToday));
            } catch (IOException e){
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }
}
