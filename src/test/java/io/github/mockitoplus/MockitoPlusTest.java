
package io.github.mockitoplus;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static io.github.mockitoplus.FailureMode.FAIL_ALTERNATING_INVOCATIONS;
import static io.github.mockitoplus.FailureMode.INTERMITTENT_FAILURES;
import static io.github.mockitoplus.FailureMode.FIRST_INVOCATION_FAILS;
import static io.github.mockitoplus.MockitoPlus.when;

public class MockitoPlusTest {
    @Test
    public void happyPath() {
        HelloWorld hello = mock(HelloWorld.class);

        when(hello.sayHello(any())).thenReturn("abc123");

        final int numIterations = 5;

        for (int i = 0; i < numIterations; i++) {
            assertThat(hello.sayHello("whatever")).isEqualTo("abc123");
        }

        verify(hello, times(numIterations)).sayHello(any());
    }

    @Test
    public void failAlternatingInvocations() {
        HelloWorld hello = mock(HelloWorld.class);

        when(hello.sayHello(any()))
                .thenReturn("abc123", FAIL_ALTERNATING_INVOCATIONS);

        // first invocation: success
        assertThat(hello.sayHello("whatever")).isEqualTo("abc123");

        // second invocation: failure
        assertFailure( () -> hello.sayHello("whatever"));

        // third invocation: success
        assertThat(hello.sayHello("whatever")).isEqualTo("abc123");

        // fourth invocation: failure
        assertFailure( () -> hello.sayHello("whatever"));

        verify(hello, times(4)).sayHello(any());
    }

    @Test
    public void failAlternatingInvocations_withCustomException() {
        HelloWorld hello = mock(HelloWorld.class);

        when(hello.sayHello(any()))
                .thenReturn("abc123",
                            FAIL_ALTERNATING_INVOCATIONS,
                            () -> createCustomException());

        // first invocation: success
        assertThat(hello.sayHello("whatever")).isEqualTo("abc123");

        // second invocation: failure
        assertFailureWithCustomException( () -> hello.sayHello("whatever"));

        // third invocation: success
        assertThat(hello.sayHello("whatever")).isEqualTo("abc123");

        // fourth invocation: failure
        assertFailureWithCustomException( () -> hello.sayHello("whatever"));

        verify(hello, times(4)).sayHello(any());
    }

    @Test
    public void firstInvocationFails() {
        HelloWorld hello = mock(HelloWorld.class);

        when(hello.sayHello(any()))
                .thenReturn("abc123", FIRST_INVOCATION_FAILS);

        // first invocation: failure
        assertFailure(() -> hello.sayHello("This should throw an exception"));

        final int numOfSuccessfulInvocations = 5;

        // all additional invocations will succeed
        for (int i = 0; i < numOfSuccessfulInvocations; i++) {
            hello.sayHello("This should succeed");
        }

        verify(hello, times(numOfSuccessfulInvocations + 1)).sayHello(any());
    }

    @Test
    public void intermittentFailures() {
        HelloWorld hello = mock(HelloWorld.class);

        when(hello.sayHello(any()))
                .thenReturn("abc123", INTERMITTENT_FAILURES);

        final int numIterations = 100;

        int successCount = 0;
        int failureCount = 0;

        for (int i = 0; i < numIterations; i++) {
          try {
              hello.sayHello("whatever");
              successCount++;
          } catch (Exception ex) {
              failureCount++;
          }
        }

        assertThat(successCount).isGreaterThanOrEqualTo(0);
        assertThat(failureCount).isGreaterThanOrEqualTo(0);
        assertThat(successCount + failureCount).isEqualTo(numIterations);
    }

    private static void assertFailure(final Callable callable) {
        assertThatCode(() -> callable.call())
                .isInstanceOf(GenericException.class)
                .hasMessage("generic exception");
    }

    private static void assertFailureWithCustomException(final Callable callable) {
        assertThatCode(() -> callable.call())
                .isInstanceOf(CustomException.class)
                .hasMessage("Things happen");
    }

    private static Exception createCustomException() {
      return new CustomException("Things happen");
    }

    private static class CustomException extends RuntimeException {
        public CustomException(String msg) {
            super(msg);
        }
    }
}

class HelloWorld {
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
