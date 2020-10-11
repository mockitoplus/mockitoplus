# mockitoplus
MockitoPlus Java library

# Example

```
import static io.github.mockitoplus.FailureMode.FAIL_ALTERNATING_INVOCATIONS;
import static io.github.mockitoplus.MockitoPlus.when;

when(hello.sayHello(any()))
          .thenReturn("bonjour", FAIL_ALTERNATING_INVOCATIONS);

```

# Example:  withFixedDelay

```
import static io.github.mockitoplus.FailureMode.FAIL_ALTERNATING_INVOCATIONS;
import static io.github.mockitoplus.MockitoPlus.when;

when(hello.sayHello(any()))
          .thenReturn("bonjour", FAIL_ALTERNATING_INVOCATIONS)
          .withFixedDelay(Duration.of(100, MILLIS));

```

# Example:  withRandomDelay

```
import static io.github.mockitoplus.FailureMode.FAIL_ALTERNATING_INVOCATIONS;
import static io.github.mockitoplus.MockitoPlus.when;

Duration max = Duration.of(800, MILLIS);

when(hello.sayHello(any()))
          .thenReturn("bonjour", FAIL_ALTERNATING_INVOCATIONS)
          .withRandomDelay(max);

```
