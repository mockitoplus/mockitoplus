# mockitoplus [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.mockitoplus/mockitoplus/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/io.github.mockitoplus/mockitoplus)

MockitoPlus Java library

# Maven usage

```
<dependency>
    <groupId>io.github.mockitoplus</groupId>
    <artifactId>mockitoplus</artifactId>
    <version>USE-LATEST-VERSION</version>
</dependency>

```

# Example: failAlternatingInvocations

```
import static org.mockito.Mockito.mock;
import static io.github.mockitoplus.MockitoPlus.when;

HelloWorld hello = mock(HelloWorld.class);

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .failAlternatingInvocations();

```

# Example: firstInvocationFails

```
import static org.mockito.Mockito.mock;
import static io.github.mockitoplus.MockitoPlus.when;

HelloWorld hello = mock(HelloWorld.class);

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .firstInvocationFails();

```

# Example: intermittentFailures

```
import static org.mockito.Mockito.mock;
import static io.github.mockitoplus.MockitoPlus.when;

HelloWorld hello = mock(HelloWorld.class);

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .intermittentFailures();

```

# Example: failEveryInvocation

```
import static org.mockito.Mockito.mock;
import static io.github.mockitoplus.MockitoPlus.when;

HelloWorld hello = mock(HelloWorld.class);

when(hello.sayHello(any()))
          .failEveryInvocation();

```

# Example: exception

```
import static org.mockito.Mockito.mock;
import static io.github.mockitoplus.MockitoPlus.when;

HelloWorld hello = mock(HelloWorld.class);

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .intermittentFailures()
          .exception(() -> new IllegalStateException("sorry"));

```

# Example:  fixedDelay

```
import static org.mockito.Mockito.mock;
import static io.github.mockitoplus.MockitoPlus.when;

HelloWorld hello = mock(HelloWorld.class);

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .fixedDelay(Duration.of(100, MILLIS));

```

# Example:  randomDelay

```
import static org.mockito.Mockito.mock;
import static io.github.mockitoplus.MockitoPlus.when;

Duration max = Duration.of(5, SECONDS);

HelloWorld hello = mock(HelloWorld.class);

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .randomDelay(max);

```
