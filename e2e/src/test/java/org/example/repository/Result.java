package org.example.repository;

public sealed interface Result<T, S> {
    record Success<T>(T value) implements Result {}
    record Failure<S>(S value) implements Result {}

}
