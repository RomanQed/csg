package com.github.romanqed.scene;

import com.github.romanqed.graphic.Frustum;
import com.github.romanqed.math.MathUtil;
import com.github.romanqed.math.Matrix4D;
import eu.mihosoft.vvecmath.Plane;

public final class PerspectiveProjection implements Projection {
    private final double fov;
    private final double near;
    private final double far;

    public PerspectiveProjection(double fov, double near, double far) {
        this.fov = fov;
        this.near = near;
        this.far = far;
    }

    public double getFov() {
        return fov;
    }

    public double getNear() {
        return near;
    }

    public double getFar() {
        return far;
    }

    @Override
    public Frustum getFrustum(Camera camera, double aspect) {
        var ret = new Frustum();
        var halfVSide = far * Math.tan(fov * 0.5);
        var halfHSide = halfVSide * aspect;
        // Camera specs
        var position = camera.getPosition();
        var front = camera.getFront();
        var right = camera.getRight();
        var up = camera.getLocalUp();
        //
        var frontMulFar = front.multiplied(far);
        // near, far
        ret.setNear(Plane.fromPointAndNormal(
                position.added(front.multiplied(near)),
                front
        ));
        ret.setFar(Plane.fromPointAndNormal(
                position.added(frontMulFar), front.negated()
        ));
        // left, right
        ret.setLeft(Plane.fromPointAndNormal(
                position,
                up.crossed(frontMulFar.added(right.multiplied(halfHSide)))
        ));
        ret.setRight(Plane.fromPointAndNormal(
                position,
                frontMulFar.subtracted(right.multiplied(halfHSide)).crossed(up)
        ));
        // top, bottom
        ret.setTop(Plane.fromPointAndNormal(
                position,
                right.crossed(frontMulFar.subtracted(up.multiplied(halfVSide)))
        ));
        ret.setBottom(Plane.fromPointAndNormal(
                position,
                frontMulFar.added(up.multiplied(halfVSide)).crossed(right)
        ));
        return ret;
    }

    @Override
    public Matrix4D toMatrix(double aspect) {
        return MathUtil.createPerspectiveMatrix(fov, aspect, near, far);
    }
}
