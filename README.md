# mockitoplus
MockitoPlus Java library

# Example: FAIL_ALTERNATING_INVOCATIONS

```
import static io.github.mockitoplus.FailureMode.FAIL_ALTERNATING_INVOCATIONS;
import static io.github.mockitoplus.MockitoPlus.when;

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .withFailureMode(FAIL_ALTERNATING_INVOCATIONS);

```

# Example: FIRST_INVOCATION_FAILS

```
import static io.github.mockitoplus.FailureMode.FIRST_INVOCATION_FAILS;
import static io.github.mockitoplus.MockitoPlus.when;

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .withFailureMode(FIRST_INVOCATION_FAILS);

```

# Example: INTERMITTENT_FAILURES

```
import static io.github.mockitoplus.FailureMode.INTERMITTENT_FAILURES;
import static io.github.mockitoplus.MockitoPlus.when;

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .withFailureMode(INTERMITTENT_FAILURES);

```

# Example:  withFixedDelay

```
import static io.github.mockitoplus.MockitoPlus.when;

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .withFixedDelay(Duration.of(100, MILLIS));

```

# Example:  withRandomDelay

```
import static io.github.mockitoplus.MockitoPlus.when;

Duration max = Duration.of(2, SECONDS);

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .withRandomDelay(max);

```
