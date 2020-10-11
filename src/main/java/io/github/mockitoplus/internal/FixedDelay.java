package io.github.mockitoplus.internal;

import java.time.Duration;

public class FixedDelay {
    public static final DelayCalculator ZERO = of(Duration.ofMillis(0));

    public static DelayCalculator of(final Duration duration) {
        return new DelayCalculator() {
            @Override
            public Duration calculateDelay() {
                return duration;
            }
        };
    }
}
