
package io.github.mockitoplus;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.Callable;

import io.github.mockitoplus.internal.GenericException;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static io.github.mockitoplus.MockitoPlus.when;

public class MockitoPlusTest {

    @Test
    public void failAlternatingInvocations() {
        HelloWorld hello = mock(HelloWorld.class);

        when(hello.sayHello(any()))
                .thenReturn("abc123")
                .failAlternatingInvocations();

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
                .thenReturn("bonjour")
                .failAlternatingInvocations()
                .exception((() -> createCustomException()));

        // first invocation: success
        assertThat(hello.sayHello("whatever")).isEqualTo("bonjour");

        // second invocation: failure
        assertFailureWithCustomException( () -> hello.sayHello("whatever"));

        // third invocation: success
        assertThat(hello.sayHello("whatever")).isEqualTo("bonjour");

        // fourth invocation: failure
        assertFailureWithCustomException( () -> hello.sayHello("whatever"));

        verify(hello, times(4)).sayHello(any());
    }

    @Test
    public void firstInvocationFails() {
        HelloWorld hello = mock(HelloWorld.class);

        when(hello.sayHello(any()))
                .thenReturn("abc123")
                .firstInvocationFails();

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
    public void failEveryInvocation() {
        HelloWorld hello = mock(HelloWorld.class);

        when(hello.sayHello(any()))
                .thenReturn("abc123")
                .failEveryInvocation();

        final int numOfInvocations = 10;

        for (int i = 0; i < numOfInvocations; i++) {
            assertFailure(() -> hello.sayHello("This should throw an exception"));
        }

        verify(hello, times(numOfInvocations)).sayHello(any());
    }

    @Test
    public void intermittentFailures() {
        HelloWorld hello = mock(HelloWorld.class);

        when(hello.sayHello(any()))
                .thenReturn("abc123")
                .intermittentFailures();

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

    @Test
    public void intermittentFailuresWithFixedDelay() {
        HelloWorld hello = mock(HelloWorld.class);

        final Duration delay = Duration.of(7, MILLIS);

        when(hello.sayHello(any()))
                .thenReturn("abc123")
                .intermittentFailures()
                .fixedDelay(delay);

        final int numIterations = 10;

        int successCount = 0;
        int failureCount = 0;


        for (int i = 0; i < numIterations; i++) {
            final long start = System.currentTimeMillis();
            try {
                hello.sayHello("whatever");
                successCount++;
            } catch (Exception ex) {
                failureCount++;
            } finally {
                final long end = System.currentTimeMillis();
                assertThat(end - start)
                        .isGreaterThanOrEqualTo(delay.toMillis());
            }
        }

        assertThat(successCount).isGreaterThanOrEqualTo(0);
        assertThat(failureCount).isGreaterThanOrEqualTo(0);
        assertThat(successCount + failureCount).isEqualTo(numIterations);
    }

    @Test
    public void intermittentFailuresWithRandomDelay() {
        HelloWorld hello = mock(HelloWorld.class);

        final Duration max = Duration.of(15, MILLIS);

        when(hello.sayHello(any()))
                .thenReturn("abc123")
                .intermittentFailures()
                .randomDelay(max);

        final int numIterations = 10;

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

    @Test
    public void testToString() {
        HelloWorld hello = mock(HelloWorld.class);

        assertThat(when(hello.sayHello(any()))
                .thenReturn("abc123")
                .toString())
                .isNotNull()
                .isNotEmpty();
    }

    private static void assertFailure(final Callable<?> callable) {
        assertThatCode(() -> callable.call())
                .isInstanceOf(GenericException.class)
                .hasMessage("generic exception");
    }

    private static void assertFailureWithCustomException(final Callable<?> callable) {
        assertThatCode(() -> callable.call())
                .isInstanceOf(CustomException.class)
                .hasMessage("Things happen");
    }

    private static Exception createCustomException() {
      return new CustomException("Things happen");
    }

    private static class CustomException extends RuntimeException {
        private static final long serialVersionUID = 1L;

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
