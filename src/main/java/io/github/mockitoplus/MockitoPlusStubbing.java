package io.github.mockitoplus;

import org.mockito.stubbing.OngoingStubbing;

public class MockitoPlusStubbing<T> {
    private OngoingStubbing<T> stubbing;

    public MockitoPlusStubbing(final OngoingStubbing<T> stubbing) {
        this.stubbing = stubbing;
    }

    public void thenReturn(final T value, final FailureMode failureMode) {
        this.thenReturn(value,
                failureMode,
                () -> makeDefaultException());
    }

    public void thenReturn(final T value,
                           final FailureMode failureMode,
                           final ExceptionFactory factory) {
        this.stubbing.thenAnswer(new MockitoPlusAnswer(value, failureMode, factory));
    }

    static private Exception makeDefaultException() {
        return new GenericException();
    }
}
