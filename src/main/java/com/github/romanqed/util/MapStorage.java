package com.github.romanqed.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class MapStorage implements PropertyStorage {
    private final Map<String, Object> body;

    public MapStorage(Supplier<Map<String, Object>> supplier) {
        this.body = supplier.get();
    }

    public MapStorage() {
        this.body = new HashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key) {
        return Optional.ofNullable((T) body.get(key));
    }

    @Override
    public void set(String key, Object value) {
        body.put(key, value);
    }

    @Override
    public boolean remove(String key) {
        return body.remove(key) != null;
    }

    @Override
    public boolean contains(String key) {
        return body.containsKey(key);
    }
}
