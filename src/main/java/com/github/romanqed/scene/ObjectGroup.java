package com.github.romanqed.scene;

import com.github.romanqed.util.AbstractConfigurable;
import com.github.romanqed.util.PropertyStorage;

import java.util.List;

public final class ObjectGroup extends AbstractConfigurable implements SceneObject {
    private final List<SceneObject> objects;

    public ObjectGroup(List<SceneObject> objects, PropertyStorage storage) {
        super(storage);
        this.objects = objects;
    }

    public ObjectGroup(List<SceneObject> objects) {
        this(objects, null);
    }

    @Override
    public void accept(SceneObjectVisitor visitor) {
        for (var object : objects) {
            object.accept(visitor);
        }
    }
}
