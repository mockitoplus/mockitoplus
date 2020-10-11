package io.github.mockitoplus;

import org.mockito.stubbing.OngoingStubbing;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import io.github.mockitoplus.internal.DelayCalculator;
import io.github.mockitoplus.internal.FixedDelay;
import io.github.mockitoplus.internal.RandomDelay;

public class MockitoPlusStubbing<T> {
    private final AtomicReference<DelayCalculator> delayCalc = new AtomicReference<DelayCalculator>(FixedDelay.ZERO);
    private OngoingStubbing<T> stubbing;

    public MockitoPlusStubbing(final OngoingStubbing<T> stubbing) {
        this.stubbing = stubbing;
    }

    public MockitoPlusStubbing<T> thenReturn(final T value, final FailureMode failureMode) {
        this.thenReturn(value,
                failureMode,
                () -> makeDefaultException());
        return this;
    }

    public MockitoPlusStubbing<T> thenReturn(final T value,
                           final FailureMode failureMode,
                           final ExceptionFactory factory) {
        this.stubbing.thenAnswer(new MockitoPlusAnswer(value, failureMode, factory, delayCalc));
        return this;
    }

    public MockitoPlusStubbing<T> withFixedDelay(final Duration duration) {
        delayCalc.set(FixedDelay.of(duration));
        return this;
    }

    public MockitoPlusStubbing<T> withRandomDelay(final Duration maxDelay) {
        delayCalc.set(new RandomDelay(new SecureRandom(), maxDelay));
        return this;
    }

    static private Exception makeDefaultException() {
        return new GenericException();
    }
}
