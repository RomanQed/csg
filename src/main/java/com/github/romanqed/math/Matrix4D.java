package com.github.romanqed.math;

public final class Matrix4D {
    double m00;
    double m01;
    double m02;
    double m03;

    double m10;
    double m11;
    double m12;
    double m13;

    double m20;
    double m21;
    double m22;
    double m23;

    double m30;
    double m31;
    double m32;
    double m33;

    public Matrix4D() {
    }

    public Matrix4D(double[] data) {
        // First row
        this.m00 = data[0];
        this.m01 = data[1];
        this.m02 = data[2];
        this.m03 = data[3];
        // Second row
        this.m10 = data[4];
        this.m11 = data[5];
        this.m12 = data[6];
        this.m13 = data[7];
        // Third row
        this.m20 = data[8];
        this.m21 = data[9];
        this.m22 = data[10];
        this.m23 = data[11];
        // Fourth row
        this.m30 = data[12];
        this.m31 = data[13];
        this.m32 = data[14];
        this.m33 = data[15];
    }

    public Matrix4D(double[][] data) {
        // First row
        this.m00 = data[0][0];
        this.m01 = data[0][1];
        this.m02 = data[0][2];
        this.m03 = data[0][3];
        // Second row
        this.m10 = data[1][0];
        this.m11 = data[1][1];
        this.m12 = data[1][2];
        this.m13 = data[1][3];
        // Third row
        this.m20 = data[2][0];
        this.m21 = data[2][1];
        this.m22 = data[2][2];
        this.m23 = data[2][3];
        // Fourth row
        this.m30 = data[3][0];
        this.m31 = data[3][1];
        this.m32 = data[3][2];
        this.m33 = data[3][3];
    }

    public static Matrix4D identity() {
        var ret = new Matrix4D();
        ret.m00 = 1D;
        ret.m11 = 1D;
        ret.m22 = 1D;
        ret.m33 = 1D;
        return ret;
    }

    public static Matrix4D multiply(Matrix4D left, Matrix4D right) {
        return left.multiply(right);
    }

    public static Vector4D multiply(Matrix4D matrix, Vector4D vector) {
        return matrix.multiply(vector);
    }

    public double[] toArray() {
        return new double[]{m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33};
    }

    public double[][] toMatrix() {
        return new double[][]{
                {m00, m01, m02, m03},
                {m10, m11, m12, m13},
                {m20, m21, m22, m23},
                {m30, m31, m32, m33}
        };
    }

    public Matrix4D multiply(Matrix4D matrix) {
        var ret = new Matrix4D();
        ret.m00 = m00 * matrix.m00 + m01 * matrix.m10 + m02 * matrix.m20 + m03 * matrix.m30;
        ret.m01 = m00 * matrix.m01 + m01 * matrix.m11 + m02 * matrix.m21 + m03 * matrix.m31;
        ret.m02 = m00 * matrix.m02 + m01 * matrix.m12 + m02 * matrix.m22 + m03 * matrix.m32;
        ret.m03 = m00 * matrix.m03 + m01 * matrix.m13 + m02 * matrix.m23 + m03 * matrix.m33;
        ret.m10 = m10 * matrix.m00 + m11 * matrix.m10 + m12 * matrix.m20 + m13 * matrix.m30;
        ret.m11 = m10 * matrix.m01 + m11 * matrix.m11 + m12 * matrix.m21 + m13 * matrix.m31;
        ret.m12 = m10 * matrix.m02 + m11 * matrix.m12 + m12 * matrix.m22 + m13 * matrix.m32;
        ret.m13 = m10 * matrix.m03 + m11 * matrix.m13 + m12 * matrix.m23 + m13 * matrix.m33;
        ret.m20 = m20 * matrix.m00 + m21 * matrix.m10 + m22 * matrix.m20 + m23 * matrix.m30;
        ret.m21 = m20 * matrix.m01 + m21 * matrix.m11 + m22 * matrix.m21 + m23 * matrix.m31;
        ret.m22 = m20 * matrix.m02 + m21 * matrix.m12 + m22 * matrix.m22 + m23 * matrix.m32;
        ret.m23 = m20 * matrix.m03 + m21 * matrix.m13 + m22 * matrix.m23 + m23 * matrix.m33;
        ret.m30 = m30 * matrix.m00 + m31 * matrix.m10 + m32 * matrix.m20 + m33 * matrix.m30;
        ret.m31 = m30 * matrix.m01 + m31 * matrix.m11 + m32 * matrix.m21 + m33 * matrix.m31;
        ret.m32 = m30 * matrix.m02 + m31 * matrix.m12 + m32 * matrix.m22 + m33 * matrix.m32;
        ret.m33 = m30 * matrix.m03 + m31 * matrix.m13 + m32 * matrix.m23 + m33 * matrix.m33;
        return ret;
    }

    public Vector4D multiply(Vector4D vector) {
        var ret = new Vector4D();
        ret.x = m00 * vector.x + m01 * vector.y + m02 * vector.z + m03 * vector.w;
        ret.y = m10 * vector.x + m11 * vector.y + m12 * vector.z + m13 * vector.w;
        ret.z = m20 * vector.x + m21 * vector.y + m22 * vector.z + m23 * vector.w;
        ret.w = m30 * vector.x + m31 * vector.y + m32 * vector.z + m33 * vector.w;
        return ret;
    }

    @Override
    public String toString() {
        return "Matrix4D{" +
                "{" + m00 + ", " + m01 + ", " + m02 + ", " + m03 + "}, " +
                "{" + m10 + ", " + m11 + ", " + m12 + ", " + m13 + "}, " +
                "{" + m20 + ", " + m21 + ", " + m22 + ", " + m23 + "}, " +
                "{" + m30 + ", " + m31 + ", " + m32 + ", " + m33 + "}" +
                "}";
    }
}
