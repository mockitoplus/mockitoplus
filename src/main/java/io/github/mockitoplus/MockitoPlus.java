package io.github.mockitoplus;

import org.mockito.Mockito;

public class MockitoPlus {
    private MockitoPlus() { }

    public static <T> MockitoPlusStubbing<T> when(T methodCall) {
        return new MockitoPlusStubbing<T>(Mockito.when(methodCall));
    }
}
