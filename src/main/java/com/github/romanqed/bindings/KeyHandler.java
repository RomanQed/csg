package com.github.romanqed.bindings;

import com.github.romanqed.jfunc.Runnable0;
import com.github.romanqed.jfunc.Runnable1;

import java.util.Objects;

public interface KeyHandler<E> {
    void register(int scancode, String name, Runnable1<E> body);

    default void register(int scancode, String name, Runnable0 body) {
        Objects.requireNonNull(body);
        register(scancode, name, e -> body.run());
    }

    void unregister(String name);

    void start();

    void stop();
}
