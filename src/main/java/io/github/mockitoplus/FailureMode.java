package io.github.mockitoplus;

public enum FailureMode {
    FIRST_INVOCATION_FAILS,
    FAIL_ALTERNATING_INVOCATIONS,
    INTERMITTENT_FAILURES,
    NEVER_FAIL,
    FAIL_EVERY_REQUEST
}
