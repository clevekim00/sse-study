package clevekim.study.spring.sse.controller;

import clevekim.study.spring.sse.model.Temperature;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rx.Subscriber;

import java.io.IOException;

/**
 * sse-study
 * <p>
 * Author: younghwan.kim
 * email : clevekim@naver.com
 * Date: 2020-02-07
 */
public class RxSseEmitter extends SseEmitter {

    static final long SSE_SECTION_TIMEOUT = 30 * 60 * 1000;

    private final Subscriber<Temperature> subscriber;

    public RxSseEmitter() {
        super(SSE_SECTION_TIMEOUT);

        this.subscriber = new Subscriber<Temperature>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Temperature temperature) {

                try {
                    RxSseEmitter.this.send(temperature);
                } catch (IOException e) {
                    unsubscribe();
                }
            }
        };

        onCompletion(subscriber::unsubscribe);
        onTimeout(subscriber::unsubscribe);
    }

    public Subscriber<Temperature> getSubscriber() {
        return subscriber;
    }
}
