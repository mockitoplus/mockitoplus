package io.github.mockitoplus;

import org.mockito.stubbing.OngoingStubbing;

public class MockitoPlusStubbing<T> {
    private OngoingStubbing stubbing;

    public MockitoPlusStubbing(final OngoingStubbing stubbing) {
        this.stubbing = stubbing;
    }

    public void thenThrow(final Exception exception) {
        this.stubbing.thenThrow(exception);
    }

    public void thenReturn(final Object value) {
        this.stubbing.thenReturn(value);
    }

    public void thenReturn(final Object value, final FailureMode failureMode) {
        this.thenReturn(value,
                failureMode,
                () -> makeDefaultException());
    }

    public void thenReturn(final Object value,
                           final FailureMode failureMode,
                           final ExceptionFactory factory) {
        this.stubbing.thenAnswer(new MockitoPlusAnswer(value, failureMode, factory));
    }

    static private Exception makeDefaultException() {
        return new GenericException();
    }
}
