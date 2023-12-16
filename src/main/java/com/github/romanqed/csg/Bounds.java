package com.github.romanqed.csg;

import eu.mihosoft.vvecmath.Vector3d;

public final class Bounds {
    private final Vector3d center;
    private final Vector3d bounds;
    private final Vector3d min;
    private final Vector3d max;

    public Bounds(Vector3d min, Vector3d max) {
        this.center = Vector3d.xyz(
                (max.x() + min.x()) / 2,
                (max.y() + min.y()) / 2,
                (max.z() + min.z()) / 2);
        this.bounds = Vector3d.xyz(
                Math.abs(max.x() - min.x()),
                Math.abs(max.y() - min.y()),
                Math.abs(max.z() - min.z()));
        this.min = min.clone();
        this.max = max.clone();
    }

    public Vector3d getCenter() {
        return center;
    }

    public Vector3d getBounds() {
        return bounds;
    }

    public boolean contains(Vertex v) {
        return contains(v.vector);
    }

    public boolean contains(Vector3d v) {
        var inX = min.x() <= v.x() && v.x() <= max.x();
        var inY = min.y() <= v.y() && v.y() <= max.y();
        var inZ = min.z() <= v.z() && v.z() <= max.z();
        return inX && inY && inZ;
    }

    public boolean intersects(Bounds b) {
        if (b.getMin().x() > this.getMax().x() || b.getMax().x() < this.getMin().x()) {
            return false;
        }
        if (b.getMin().y() > this.getMax().y() || b.getMax().y() < this.getMin().y()) {
            return false;
        }
        return !(b.getMin().z() > this.getMax().z()) && !(b.getMax().z() < this.getMin().z());
    }

    public Vector3d getMin() {
        return min;
    }

    public Vector3d getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "[center: " + center + ", bounds: " + bounds + "]";
    }
}
