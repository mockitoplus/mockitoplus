package io.github.mockitoplus;

import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

public class MockitoPlus {
    private MockitoPlus() { /* private constructor */ }

    public static <T> MockitoPlusStubbing<T> when(T methodCall) {
        OngoingStubbing<T> stubbing = Mockito.when(methodCall);
        return new MockitoPlusStubbing<T>(stubbing);
    }
}
