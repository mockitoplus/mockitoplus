package io.github.mockitoplus;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.github.mockitoplus.internal.DelayCalculator;
import io.github.mockitoplus.internal.FailureMode;

public class MockitoPlusAnswer<T> implements Answer<T> {
    private final T value;
    private final ExceptionFactory exceptionFactory;
    private final AtomicInteger invocationCount = new AtomicInteger(0);
    private final Random random = new SecureRandom();
    private final AtomicReference<FailureMode> failureMode;
    private final AtomicReference<DelayCalculator> delayCalculator;

    public MockitoPlusAnswer(T value,
                             AtomicReference<FailureMode> failureMode,
                             ExceptionFactory exFactory,
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
        } else if (failureMode.get() == FailureMode.NEVER_FAIL) {
            return value;
        } else {
            throw new IllegalArgumentException("unknown FailureMode: " + failureMode);
        }
        return value;
    }

    private void onFailure(final InvocationOnMock handler) throws Exception {
        throw exceptionFactory.createException();
    }
}
