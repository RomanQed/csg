package com.github.romanqed.csg;

import com.github.romanqed.primitive.Primitive;

public interface SolidFactory {
    Solid create(Primitive primitive);
}
