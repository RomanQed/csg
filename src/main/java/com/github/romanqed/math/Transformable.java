package com.github.romanqed.math;

import eu.mihosoft.vvecmath.Transform;

public interface Transformable<T extends Transformable<T>> {
    T apply(Transform transform);
}
