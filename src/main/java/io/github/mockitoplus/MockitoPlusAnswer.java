package io.github.mockitoplus;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MockitoPlusAnswer implements Answer {
    private final Object value;
    private final Exception exception;
    private final FailureMode failureMode;
    private final AtomicInteger invocationCount = new AtomicInteger(0);
    private final Random random = new SecureRandom();

    public MockitoPlusAnswer(Object value, Exception exception, FailureMode failureMode) {
        this.value = value;
        this.exception = exception;
        this.failureMode = failureMode;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        final int n = invocationCount.getAndIncrement();
        if (failureMode == FailureMode.FAIL_ALTERNATING_INVOCATIONS) {
            if (n % 2 == 0) {
                return value;
            } else {
                onFailure(invocation);
            }
        } else if (failureMode == FailureMode.FIRST_INVOCATION_FAILS) {
            if (n > 0) {
                return value;
            } else {
                onFailure(invocation);
            }
        } else if (failureMode == FailureMode.INTERMITTENT_FAILURES) {
            if (random.nextDouble() >= 0.1) {
                return value;
            } else {
                onFailure(invocation);
            }
        } else {
            throw new IllegalArgumentException("unknown FailureMode: " + failureMode);
        }
        return value;
    }

    private void onFailure(InvocationOnMock handler) throws Exception {
        throw exception;
    }
}
