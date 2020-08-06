package com.github.guqt178.binding.command;

public interface BindingFunction<T, R> {

    R apply(T t);
}