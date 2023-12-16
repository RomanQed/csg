package com.github.romanqed.math;

import eu.mihosoft.vvecmath.Vector3d;

public final class MathUtil {
    private MathUtil() {
    }

    public static Vector3d calculateNormal(Vector3d a, Vector3d b, Vector3d c) {
        return b.minus(a).crossed(c.minus(a)).normalized();
    }

    public static Matrix4D createViewportMatrix(double x, double y, double width, double height) {
        var ret = new Matrix4D();
        ret.m00 = width / 2D;
        ret.m03 = x + width / 2D;
        ret.m11 = height / 2D;
        ret.m13 = y + height / 2D;
        ret.m22 = 1D;
        ret.m33 = 1D;
        return ret;
    }

    public static Matrix4D createLookAtMatrix(Vector3d eye, Vector3d center, Vector3d up) {
        var z = center.subtracted(eye).normalized();
        var x = up.crossed(z).normalized();
        var y = z.crossed(x);
        var negated = eye.negated();
        var ret = new Matrix4D();
        // First row
        ret.m00 = x.x();
        ret.m01 = x.y();
        ret.m02 = x.z();
        ret.m03 = x.dot(negated);
        // Second row
        ret.m10 = y.x();
        ret.m11 = y.y();
        ret.m12 = y.z();
        ret.m13 = y.dot(negated);
        // Third row
        ret.m20 = z.x();
        ret.m21 = z.y();
        ret.m22 = z.z();
        ret.m23 = z.dot(negated);
        // Fourth row
        ret.m33 = 1D;
        return ret;
    }

    public static Matrix4D createPerspectiveMatrix(double fov, double aspect, double near, double far) {
        var tanHalfFov = Math.tan(fov / 2D);
        var ret = new Matrix4D();
        ret.m00 = 1D / (aspect * tanHalfFov);
        ret.m11 = 1D / tanHalfFov;
        ret.m22 = -(far + near) / (far - near);
        ret.m23 = -(2D * far * near) / (far - near);
        ret.m32 = -1D;
        return ret;
    }
}
