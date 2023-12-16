package com.github.romanqed.math;

import eu.mihosoft.vvecmath.Vector3d;

public final class Line {
    private final Vector3d direction;
    private final Vector3d zero;

    public Line(Vector3d start, Vector3d end) {
        var delta = end.subtracted(start);
        var norm2 = delta.magnitudeSq();
        if (norm2 == 0D) {
            throw new IllegalArgumentException("Zero norm");
        }
        this.direction = delta.multiplied(1D / Math.sqrt(norm2));
        this.zero = start.added(delta.multiplied(-start.dot(delta)));
    }

    private Line(Line line) {
        this.direction = line.direction;
        this.zero = line.zero;
    }

    public Vector3d getZero() {
        return zero;
    }

    public Vector3d getDirection() {
        return direction;
    }

    public double getAbscissa(Vector3d point) {
        return point.subtracted(zero).dot(direction);
    }

    public Vector3d pointAt(double abscissa) {
        return zero.added(direction.multiplied(abscissa));
    }
}
