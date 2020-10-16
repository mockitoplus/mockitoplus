package io.github.mockitoplus;

import org.mockito.stubbing.OngoingStubbing;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import io.github.mockitoplus.internal.DelayCalculator;
import io.github.mockitoplus.internal.FixedDelay;
import io.github.mockitoplus.internal.GenericException;
import io.github.mockitoplus.internal.MockitoPlusAnswer;
import io.github.mockitoplus.internal.RandomDelay;

public class MockitoPlusStubbing<T> {
    private static final ExceptionFactory DEFAULT_EXCEPTION_FACTORY = new ExceptionFactory() {
        @Override
        public Exception createException() {
            return new GenericException();
        }
    };

    private final AtomicReference<DelayCalculator> delayCalc = new AtomicReference<DelayCalculator>(FixedDelay.ZERO);
    private final AtomicReference<FailureMode> failureMode = new AtomicReference<FailureMode>(FailureMode.NEVER_FAIL);
    private final AtomicReference<ExceptionFactory> exceptionFactory = new AtomicReference<ExceptionFactory>(DEFAULT_EXCEPTION_FACTORY);
    private final OngoingStubbing<T> stubbing;

    public MockitoPlusStubbing(final OngoingStubbing<T> stubbing) {
        this.stubbing = stubbing;
    }

    public MockitoPlusStubbing<T> thenReturn(final T value) {
        this.stubbing.thenAnswer(new MockitoPlusAnswer(value, failureMode, exceptionFactory, delayCalc));
        return this;
    }

    public MockitoPlusStubbing<T> firstInvocationFails() {
        return failureMode(FailureMode.FIRST_INVOCATION_FAILS);
    }

    public MockitoPlusStubbing<T> failAlternatingInvocations() {
        return failureMode(FailureMode.FAIL_ALTERNATING_INVOCATIONS);
    }

    public MockitoPlusStubbing<T> intermittentFailures() {
        return failureMode(FailureMode.INTERMITTENT_FAILURES);
    }

    public MockitoPlusStubbing<T> exception(final ExceptionFactory factory) {
        this.exceptionFactory.set(factory);
        return this;
    }

    public MockitoPlusStubbing<T> failureMode(final FailureMode mode) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("failureMode=" + failureMode.get().name());
        return sb.toString();
    }
}
