package io.github.mockitoplus.internal;

import java.time.Duration;
import java.util.Iterator;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MILLIS;

public class RandomDelay implements DelayCalculator {
    private final Iterator<Long> longIterator;

    public RandomDelay(final Random random, final Duration lowerBound, final Duration upperBound) {
        longIterator = random.longs(lowerBound.toMillis(), upperBound.toMillis()).iterator();
    }

    @Override
    public Duration calculateDelay() {
        final long n = longIterator.next();
        System.out.println("n: " + n);
        return Duration.of(n, MILLIS);
    }
}
