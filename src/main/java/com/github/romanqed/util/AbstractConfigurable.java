package com.github.romanqed.util;

public abstract class AbstractConfigurable implements Configurable {
    protected PropertyStorage storage;

    protected AbstractConfigurable(PropertyStorage storage) {
        this.storage = storage;
    }

    @Override
    public PropertyStorage getStorage() {
        return storage;
    }

    @Override
    public void setStorage(PropertyStorage storage) {
        this.storage = storage;
    }
}
