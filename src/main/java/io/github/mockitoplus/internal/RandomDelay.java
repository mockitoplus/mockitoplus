package io.github.mockitoplus.internal;

import java.time.Duration;
import java.util.Iterator;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MILLIS;

public class RandomDelay implements DelayCalculator {
    private final Iterator<Long> longIterator;

    public RandomDelay(final Random random, final Duration max) {
        longIterator = random.longs(0, max.toMillis()).iterator();
    }

    @Override
    public Duration calculateDelay() {
        final long n = longIterator.next();
        return Duration.of(n, MILLIS);
    }
}
