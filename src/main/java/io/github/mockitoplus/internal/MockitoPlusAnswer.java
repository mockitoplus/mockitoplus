package io.github.mockitoplus.internal;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import io.github.mockitoplus.ExceptionFactory;
import io.github.mockitoplus.FailureMode;

public class MockitoPlusAnswer<T> implements Answer<T> {
    private final T value;
    private final AtomicInteger invocationCount = new AtomicInteger(0);
    private final AtomicReference<ExceptionFactory> exceptionFactory;
    private final AtomicReference<FailureMode> failureMode;
    private final AtomicReference<DelayCalculator> delayCalculator;
    private final Random random = new SecureRandom();

    public MockitoPlusAnswer(T value,
                             AtomicReference<FailureMode> failureMode,
                             AtomicReference<ExceptionFactory> exFactory,
                             AtomicReference<DelayCalculator> delayCalculator) {
        this.value = value;
        this.exceptionFactory = exFactory;
        this.failureMode = failureMode;
        this.delayCalculator = delayCalculator;
    }

    @Override
    public T answer(InvocationOnMock invocation) throws Throwable {
        Thread.sleep(delayCalculator.get().calculateDelay().toMillis());
        final int n = invocationCount.getAndIncrement();
        if (failureMode.get() == FailureMode.FAIL_ALTERNATING_INVOCATIONS) {
            if (n % 2 == 0) {
                return value;
            } else {
                onFailure(invocation);
            }
        } else if (failureMode.get() == FailureMode.FIRST_INVOCATION_FAILS) {
            if (n > 0) {
                return value;
            } else {
                onFailure(invocation);
            }
        } else if (failureMode.get() == FailureMode.INTERMITTENT_FAILURES) {
            if (random.nextDouble() >= 0.1) {
                return value;
            } else {
                onFailure(invocation);
            }
        } else if (failureMode.get() == FailureMode.FAIL_ALL_INVOCATIONS) {
            onFailure(invocation);
        } else if (failureMode.get() == FailureMode.NEVER_FAIL) {
            return value;
        } else {
            throw new IllegalArgumentException("unknown FailureMode: " + failureMode);
        }
        return value;
    }

    private void onFailure(final InvocationOnMock handler) throws Exception {
        throw exceptionFactory.get().createException();
    }
}
