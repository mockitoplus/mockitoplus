package io.github.mockitoplus;

@FunctionalInterface
public interface ExceptionFactory {
    Exception createException();
}
