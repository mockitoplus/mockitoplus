package io.github.mockitoplus;

import org.mockito.stubbing.OngoingStubbing;

public class MockitoPlusStubbing<T> {
    private OngoingStubbing stubbing;

    public MockitoPlusStubbing(final OngoingStubbing stubbing) {
        this.stubbing = stubbing;
    }

    public void thenReturn(final Object value) {
        this.stubbing.thenReturn(value);
    }

    public void thenThrow(final Exception exception) {
        this.stubbing.thenThrow(exception);
    }

    public void thenReturn(final Object value, final FailureMode failureMode) {
        thenReturn(value, makeDefaultException(), failureMode);
    }

    private void thenReturn(final Object value, Exception exception, final FailureMode failureMode) {
        this.stubbing.thenAnswer(new MockitoPlusAnswer(value, exception, failureMode));
    }

    static private Exception makeDefaultException() {
        return new GenericException();
    }
}
