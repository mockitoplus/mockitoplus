# mockitoplus
MockitoPlus Java library

# Example: failAlternatingInvocations

```
import static io.github.mockitoplus.MockitoPlus.when;

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .failAlternatingInvocations();

```

# Example: firstInvocationFails

```
import static io.github.mockitoplus.MockitoPlus.when;

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .firstInvocationFails();

```

# Example: intermittentFailures

```
import static io.github.mockitoplus.MockitoPlus.when;

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .intermittentFailures();

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
