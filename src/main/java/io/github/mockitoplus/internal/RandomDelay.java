package io.github.mockitoplus.internal;

import java.time.Duration;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.LongStream;

import static java.time.temporal.ChronoUnit.MILLIS;

public class RandomDelay implements DelayCalculator {
    private final Iterator<Long> longIterator;

    public RandomDelay(final Random random, final Duration max) {
        LongStream longStream = random.longs(0, max.toMillis());
        longIterator = longStream.iterator();
    }

    @Override
    public Duration calculateDelay() {
        final long n = longIterator.next();
        return Duration.of(n, MILLIS);
    }
}
