# mockitoplus
Mockito Plus library

# Example

```
import static io.github.mockitoplus.FailureMode.FAIL_ALTERNATING_INVOCATIONS;
import static io.github.mockitoplus.MockitoPlus.when;

when(hello.sayHello(any()))
          .thenReturn("bonjour", FAIL_ALTERNATING_INVOCATIONS);

```
