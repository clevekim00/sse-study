package clevekim.study.spring.sse.component;

import clevekim.study.spring.sse.model.Temperature;
import com.sun.javafx.scene.SceneEventDispatcher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * sse-study
 * <p>
 * Author: younghwan.kim
 * email : clevekim@naver.com
 * Date: 2020-02-05
 */
@Component
public class TemperatureSensor {

    private final ApplicationEventPublisher publisher;
    private final Random rnd = new Random();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public TemperatureSensor(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostConstruct
    public void startProcessing() {
        this.executor.schedule(this::prob, 1, TimeUnit.SECONDS);
    }

    private void prob() {
        double temperature = 16 + rnd.nextGaussian() * 10;
        publisher.publishEvent(new Temperature(temperature));

        executor.schedule(this::prob, rnd.nextInt(5000), TimeUnit.MILLISECONDS);
    }
}
