package com.github.romanqed.bindings;

import com.github.romanqed.jfunc.Exceptions;
import com.github.romanqed.jfunc.Runnable1;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractKeyHandler<E> implements KeyHandler<E> {
    private final Map<String, Runnable1<E>> entries;
    private final Map<Integer, String> bindings;
    private final Map<String, Integer> scancodes;
    private final Object lock;

    protected AbstractKeyHandler() {
        this.entries = new HashMap<>();
        this.bindings = new HashMap<>();
        this.scancodes = new HashMap<>();
        this.lock = new Object();
    }

    protected void handle(int scancode, E event) {
        synchronized (lock) {
            var binding = bindings.get(scancode);
            if (binding == null) {
                return;
            }
            var entry = entries.get(binding);
            Exceptions.suppress(() -> entry.run(event));
        }
    }

    @Override
    public void register(int scancode, String name, Runnable1<E> body) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(body);
        synchronized (lock) {
            entries.put(name, body);
            bindings.put(scancode, name);
            scancodes.put(name, scancode);
        }
    }

    @Override
    public void unregister(String name) {
        Objects.requireNonNull(name);
        synchronized (lock) {
            entries.remove(name);
            var scancode = scancodes.remove(name);
            if (scancode == null) {
                return;
            }
            bindings.remove(scancode);
        }
    }
}
