package clevekim.study.spring.sse.controller;

/**
 * sse-study
 * <p>
 * Author: younghwan.kim
 * email : clevekim@naver.com
 * Date: 2020-02-05
 */

import clevekim.study.spring.sse.component.TemperatureSensor;
import clevekim.study.spring.sse.model.Temperature;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class TemperatureController {

    private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

    private TemperatureSensor temperatureSensor;
    public TemperatureController(TemperatureSensor temperatureSensor) {
        this.temperatureSensor = temperatureSensor;
    }

    @RequestMapping(value = "/temperature-stream", method = RequestMethod.GET)
    public SseEmitter events(HttpServletRequest request) {
//        SseEmitter emitter = new SseEmitter();
//        clients.add(emitter);
//
//        emitter.onTimeout(() -> clients.remove(emitter));
//        emitter.onCompletion(() -> clients.remove(emitter));
//
//        return emitter;

        RxSseEmitter emitter = new RxSseEmitter();

        temperatureSensor.temperatureStream().subscribe(emitter.getSubscriber());

        return emitter;
    }

    @Async
    @EventListener
    public void handleMessage(Temperature temperature) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        clients.forEach(emitter -> {
            try {
                emitter.send(temperature, MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });
        clients.removeAll(deadEmitters);
    }
}
