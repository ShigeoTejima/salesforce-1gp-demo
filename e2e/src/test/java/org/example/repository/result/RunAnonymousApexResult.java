package org.example.repository.result;

public record RunAnonymousApexResult(
    Integer line,
    Integer column,
    boolean compiled,
    boolean success,
    String compileProblem,
    String exceptionStackTrace,
    String exceptionMessage
) {
}
