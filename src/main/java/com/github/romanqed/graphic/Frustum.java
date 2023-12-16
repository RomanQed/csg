package com.github.romanqed.graphic;

import eu.mihosoft.vvecmath.Plane;

public final class Frustum {
    Plane top;
    Plane bottom;
    Plane right;
    Plane left;
    Plane far;
    Plane near;

    public Plane getTop() {
        return top;
    }

    public void setTop(Plane top) {
        this.top = top;
    }

    public Plane getBottom() {
        return bottom;
    }

    public void setBottom(Plane bottom) {
        this.bottom = bottom;
    }

    public Plane getRight() {
        return right;
    }

    public void setRight(Plane right) {
        this.right = right;
    }

    public Plane getLeft() {
        return left;
    }

    public void setLeft(Plane left) {
        this.left = left;
    }

    public Plane getFar() {
        return far;
    }

    public void setFar(Plane far) {
        this.far = far;
    }

    public Plane getNear() {
        return near;
    }

    public void setNear(Plane near) {
        this.near = near;
    }
}
