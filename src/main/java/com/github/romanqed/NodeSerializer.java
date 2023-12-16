package com.github.romanqed;

public interface NodeSerializer {
    String serialize(SolidNode node);

    SolidNode deserialize(String spec);
}
