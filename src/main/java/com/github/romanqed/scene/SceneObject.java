package com.github.romanqed.scene;

import com.github.romanqed.util.Configurable;

public interface SceneObject extends Configurable {
    void accept(SceneObjectVisitor visitor);
}
