package kang.onezero.tobyspring.learningtest.template;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
