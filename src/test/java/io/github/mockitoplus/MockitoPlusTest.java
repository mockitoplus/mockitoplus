
package io.github.mockitoplus;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static io.github.mockitoplus.MockitoPlus.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static io.github.mockitoplus.FailureMode.FAIL_ALTERNATING_INVOCATIONS;
import static io.github.mockitoplus.FailureMode.INTERMITTENT_FAILURES;
import static io.github.mockitoplus.FailureMode.FIRST_INVOCATION_FAILS;

public class MockitoPlusTest {
    @Test
    public void happyPath() {
        HelloWorld hello = mock(HelloWorld.class);

        when(hello.sayHello(any())).thenReturn("abc123");

        final int numIterations = 5;

        for (int i = 0; i < numIterations; i++) {
            assertEquals("abc123", hello.sayHello("whatever"));
        }

        verify(hello, times(numIterations)).sayHello(any());
    }

    @Test
    public void failAlternatingInvocations() {
        HelloWorld hello = mock(HelloWorld.class);

        when(hello.sayHello(any()))
                .thenReturn("abc123", FAIL_ALTERNATING_INVOCATIONS);

        // first invocation: success
        assertEquals("abc123", hello.sayHello("whatever"));

        // second invocation: failure
        assertFailure( () -> hello.sayHello("whatever"));

        // third invocation: success
        assertEquals("abc123", hello.sayHello("whatever"));

        // fourth invocation: failure
        assertFailure( () -> hello.sayHello("whatever"));

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
}

class HelloWorld {
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
