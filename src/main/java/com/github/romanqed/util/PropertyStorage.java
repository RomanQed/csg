package com.github.romanqed.util;

import java.util.Optional;

public interface PropertyStorage {
    <T> Optional<T> get(String key);

    void set(String key, Object value);

    boolean remove(String key);

    boolean contains(String key);
}
