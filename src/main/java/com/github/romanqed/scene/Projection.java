package com.github.romanqed.scene;

import com.github.romanqed.graphic.Frustum;
import com.github.romanqed.math.Matrix4D;

public interface Projection {
    Frustum getFrustum(Camera camera, double aspect);

    Matrix4D toMatrix(double aspect);
}
