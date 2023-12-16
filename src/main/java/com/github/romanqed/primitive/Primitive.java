package com.github.romanqed.primitive;

import com.github.romanqed.csg.Polygon;
import com.github.romanqed.util.Configurable;

import java.util.List;

public interface Primitive extends Configurable {
    List<Polygon> toPolygons();
}
