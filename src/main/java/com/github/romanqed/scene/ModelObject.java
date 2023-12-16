package com.github.romanqed.scene;

import com.github.romanqed.graphic.Tris;
import com.github.romanqed.util.AbstractConfigurable;
import com.github.romanqed.util.PropertyStorage;

import java.util.ArrayList;
import java.util.List;

public final class ModelObject extends AbstractConfigurable implements SceneObject {
    private final List<Tris> trises;

    public ModelObject(List<Tris> trises, PropertyStorage storage) {
        super(storage);
        this.trises = new ArrayList<>(trises);
    }

    public ModelObject(List<Tris> trises) {
        this(trises, null);
    }

    public List<Tris> getTrises() {
        return trises;
    }

    @Override
    public void accept(SceneObjectVisitor visitor) {
        visitor.visit(this);
    }
}
