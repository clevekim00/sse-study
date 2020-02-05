package clevekim.study.spring.sse.model;

import lombok.*;

/**
 * sse-study
 * <p>
 * Author: younghwan.kim
 * email : clevekim@naver.com
 * Date: 2020-02-05
 */
@Builder
@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Temperature {

    private double value;

    public Temperature(double d) {
        this.value = d;
    }
}
