package com.github.romanqed.scene;

import eu.mihosoft.vvecmath.Vector3d;

import java.util.ArrayList;
import java.util.List;

public final class Scene {
    private final List<SceneObject> objects;
    private final Camera camera;
    private final Projection projection;

    public Scene(List<SceneObject> objects, Camera camera, Projection projection) {
        this.objects = new ArrayList<>(objects);
        this.camera = camera;
        this.projection = projection;
    }

    public Scene(List<SceneObject> objects, Projection projection) {
        this(objects, new Camera(Vector3d.ZERO, Vector3d.Y_ONE), projection);
    }

    public Scene(Camera camera, Projection projection) {
        this.camera = camera;
        this.projection = projection;
        this.objects = new ArrayList<>();
    }

    public Scene(Projection projection) {
        this(new Camera(Vector3d.ZERO, Vector3d.Y_ONE), projection);
    }

    public List<SceneObject> getObjects() {
        return objects;
    }

    public SceneObject getObject(int index) {
        return this.objects.get(index);
    }

    public int addObject(SceneObject object) {
        var ret = this.objects.size();
        this.objects.add(object);
        return ret;
    }

    public boolean removeObject(SceneObject object) {
        return this.objects.remove(object);
    }

    public Camera getCamera() {
        return camera;
    }

    public Projection getProjection() {
        return projection;
    }
}
