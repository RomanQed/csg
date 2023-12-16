package com.github.romanqed.math;

import eu.mihosoft.vvecmath.Plane;
import eu.mihosoft.vvecmath.Vector3d;

public final class PlaneUtil {
    public static final double DEFAULT_TOLERANCE = 1e-12;

    private PlaneUtil() {
    }

    public static Vector3d intersection(Plane plane, Line line, double tolerance) {
        var normal = plane.getNormal();
        var direction = line.getDirection();
        var dot = normal.dot(direction);
        if (Math.abs(dot) < tolerance) {
            return null;
        }
        var point = line.pointAt(0);
        var offset = -plane.getAnchor().dot(normal);
        var k = -(offset + normal.dot(point)) / dot;
        return point.added(direction.multiplied(k));
    }

    public static Vector3d intersection(Plane plane, Line line) {
        return intersection(plane, line, DEFAULT_TOLERANCE);
    }
}
