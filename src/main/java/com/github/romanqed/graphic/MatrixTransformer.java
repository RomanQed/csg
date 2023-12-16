package com.github.romanqed.graphic;

import com.github.romanqed.math.Matrix4D;
import com.github.romanqed.math.Vector4D;
import eu.mihosoft.vvecmath.Vector3d;

public final class MatrixTransformer implements Transformer {
    private Matrix4D matrix;

    public MatrixTransformer(Matrix4D matrix) {
        this.matrix = matrix;
    }

    public MatrixTransformer() {
        this.matrix = Matrix4D.identity();
    }

    public Matrix4D getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix4D matrix) {
        this.matrix = matrix;
    }

    private Vector3d transform(Vector3d vector) {
        var value = new Vector4D(vector);
        var ret = matrix.multiply(value);
        return ret.to3D();
    }

    @Override
    public Triangle transform(Triangle triangle) {
        return new Triangle(
                transform(triangle.first),
                transform(triangle.second),
                transform(triangle.third)
        );
    }
}
