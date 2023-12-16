package com.github.romanqed.util;

public interface Configurable {
    PropertyStorage getStorage();

    void setStorage(PropertyStorage storage);
}
