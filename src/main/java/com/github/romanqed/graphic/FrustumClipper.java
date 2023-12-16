package com.github.romanqed.graphic;

import eu.mihosoft.vvecmath.Vector3d;

import java.util.Collections;
import java.util.HashSet;

public final class FrustumClipper implements Clipper {
    private Frustum frustum;

    public FrustumClipper(Frustum frustum) {
        this.frustum = frustum;
    }

    public FrustumClipper() {
        this(null);
    }

    public Frustum getFrustum() {
        return frustum;
    }

    public void setFrustum(Frustum frustum) {
        this.frustum = frustum;
    }

    private boolean isOutside(Vector3d v1, Vector3d v2, Vector3d v3) {
        // near, far
        if (frustum.near.compare(v1) == -1 && frustum.near.compare(v2) == -1 && frustum.near.compare(v3) == -1) {
            return true;
        }
        if (frustum.far.compare(v1) == -1 && frustum.far.compare(v2) == -1 && frustum.far.compare(v3) == -1) {
            return true;
        }
        // left, right
        if (frustum.left.compare(v1) == -1 && frustum.left.compare(v2) == -1 && frustum.left.compare(v3) == -1) {
            return true;
        }
        if (frustum.right.compare(v1) == -1 && frustum.right.compare(v2) == -1 && frustum.right.compare(v3) == -1) {
            return true;
        }
        // top, bottom
        if (frustum.top.compare(v1) == -1 && frustum.top.compare(v2) == -1 && frustum.top.compare(v3) == -1) {
            return true;
        }
        return frustum.bottom.compare(v1) == -1
                && frustum.bottom.compare(v2) == -1
                && frustum.bottom.compare(v3) == -1;
    }

    @Override
    public Iterable<Triangle> clip(Triangle triangle) {
        // Try to clip by full frustum
        if (isOutside(triangle.first, triangle.second, triangle.third)) {
            return Collections.emptyList();
        }
        // Clip by near plane
        var clipped = ClipUtil.clip(frustum.near, triangle);
        var ret = new HashSet<>(clipped);
        // Clip clipped triangles by far plane
        for (var tri : clipped) {
            ret.addAll(ClipUtil.clip(frustum.far, tri));
        }
        return ret;
    }
}
