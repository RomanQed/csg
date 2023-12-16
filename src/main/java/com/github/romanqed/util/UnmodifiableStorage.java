package com.github.romanqed.util;

import java.util.Objects;
import java.util.Optional;

public final class UnmodifiableStorage implements PropertyStorage {
    private final PropertyStorage body;

    public UnmodifiableStorage(PropertyStorage storage) {
        this.body = Objects.requireNonNull(storage);
    }

    @Override
    public <T> Optional<T> get(String key) {
        return body.get(key);
    }

    @Override
    public void set(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(String key) {
        return body.contains(key);
    }
}
