package com.github.romanqed.scene;

import com.github.romanqed.math.MathUtil;
import com.github.romanqed.math.Matrix4D;
import eu.mihosoft.vvecmath.Vector3d;

import java.util.Objects;

public final class Camera {
    private final Vector3d up;
    private Vector3d position;
    private Vector3d front;
    private Vector3d localUp;
    private Vector3d right;
    private double yaw;
    private double pitch;

    public Camera(Vector3d position, Vector3d up) {
        this.position = Objects.requireNonNull(position);
        this.up = Objects.requireNonNull(up);
        this.yaw = 0;
        this.pitch = 0;
        this.update();
    }

    private static double normalize(double angle) {
        var value = Math.abs(angle);
        var sign = angle / value;
        if (sign < 0) {
            return 360 - value % 360;
        }
        return value % 360;
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public Vector3d getUp() {
        return up;
    }

    public Vector3d getFront() {
        return front;
    }

    public Vector3d getRight() {
        return right;
    }

    public Vector3d getLocalUp() {
        return localUp;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
        this.update();
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
        this.update();
    }

    public void rotate(double yaw, double pitch) {
        this.yaw += yaw;
        this.pitch += pitch;
        this.update();
    }

    public void rotateYaw(double offset) {
        this.yaw += offset;
        this.update();
    }

    public void rotatePitch(double offset) {
        this.pitch += offset;
        this.update();
    }

    public void move(Vector3d offset) {
        this.move(offset.x(), offset.y(), offset.z());
    }

    public void move(double x, double y, double z) {
        position = position
                .added(right.multiplied(x))
                .added(localUp.multiplied(y))
                .added(front.multiplied(z));
    }

    public void moveX(double offset) {
        position = position.added(right.multiplied(offset));
    }

    public void moveY(double offset) {
        position = position.added(localUp.multiplied(offset));
    }

    public void moveZ(double offset) {
        position = position.added(front.multiplied(offset));
    }

    public Matrix4D toMatrix() {
        return MathUtil.createLookAtMatrix(position, position.added(front), up);
    }

    private void update() {
        this.yaw = normalize(yaw);
        this.pitch = normalize(pitch);
        var direction = Vector3d.xyz(
                Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)),
                Math.sin(Math.toRadians(pitch)),
                Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch))
        );
        this.front = direction.normalized();
        this.right = front.crossed(up).normalized();
        this.localUp = right.crossed(front).normalized();
    }
}
