package com.github.romanqed.graphic;

public interface Clipper {
    Iterable<Triangle> clip(Triangle triangle);
}
