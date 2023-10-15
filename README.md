# mockitoplus [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.mockitoplus/mockitoplus/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/io.github.mockitoplus/mockitoplus)

MockitoPlus Java library

# Maven usage

```
<dependency>
    <groupId>io.github.mockitoplus</groupId>
    <artifactId>mockitoplus</artifactId>
    <version>USE-LATEST-VERSION</version>
    <scope>test</scope>
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

Duration lowerBound = Duration.of(1, SECONDS);
Duration upperBound = Duration.of(20, SECONDS);

HelloWorld hello = mock(HelloWorld.class);

when(hello.sayHello(any()))
          .thenReturn("bonjour")
          .randomDelay(lowerBound, upperBound);

```

## How to release a new version?

1. Every change on the main development branch is released as `-SNAPSHOT` version to Sonatype snapshot repo
   at https://oss.sonatype.org/content/repositories/snapshots/io/github/mockitplus/mockitoplus
2. In order to release a non-snapshot version to Maven Central push an annotated tag, for example:

```
git tag -a -m "Release 0.6.0" v0.6.0
git push origin v0.6.0
```

3. At the moment, you **may not create releases from GitHub Web UI**. Doing so will make the CI build fail because the
   CI creates the changelog and posts to GitHub releases. We'll support this in the future.
