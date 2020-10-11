package io.github.mockitoplus;

import org.mockito.stubbing.OngoingStubbing;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import io.github.mockitoplus.internal.DelayCalculator;
import io.github.mockitoplus.internal.FailureMode;
import io.github.mockitoplus.internal.FixedDelay;
import io.github.mockitoplus.internal.GenericException;
import io.github.mockitoplus.internal.RandomDelay;

public class MockitoPlusStubbing<T> {
    private final AtomicReference<DelayCalculator> delayCalc = new AtomicReference<DelayCalculator>(FixedDelay.ZERO);
    private final AtomicReference<FailureMode> failureMode = new AtomicReference<FailureMode>(FailureMode.NEVER_FAIL);
    private OngoingStubbing<T> stubbing;

    public MockitoPlusStubbing(final OngoingStubbing<T> stubbing) {
        this.stubbing = stubbing;
    }

    public MockitoPlusStubbing<T> thenReturn(final T value) {
        this.thenReturn(value,
                () -> makeDefaultException());
        return this;
    }

    public MockitoPlusStubbing<T> thenReturn(final T value,
                                             final ExceptionFactory factory) {

        this.stubbing.thenAnswer(new MockitoPlusAnswer(value, failureMode, factory, delayCalc));
        return this;
    }

    public MockitoPlusStubbing<T> firstInvocationFails() {
        return withFailureMode(FailureMode.FIRST_INVOCATION_FAILS);
    }

    public MockitoPlusStubbing<T> failAlternatingInvocations() {
        return withFailureMode(FailureMode.FAIL_ALTERNATING_INVOCATIONS);
    }

    public MockitoPlusStubbing<T> intermittentFailures() {
        return withFailureMode(FailureMode.INTERMITTENT_FAILURES);
    }

    private MockitoPlusStubbing<T> withFailureMode(final FailureMode mode) {
        failureMode.set(mode);
        return this;
    }

    public MockitoPlusStubbing<T> fixedDelay(final Duration duration) {
        delayCalc.set(FixedDelay.of(duration));
        return this;
    }

    public MockitoPlusStubbing<T> randomDelay(final Duration maxDelay) {
        delayCalc.set(new RandomDelay(new SecureRandom(), maxDelay));
        return this;
    }

    static private Exception makeDefaultException() {
        return new GenericException();
    }
}
