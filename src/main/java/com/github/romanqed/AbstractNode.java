package com.github.romanqed;

import com.github.romanqed.csg.Solid;
import eu.mihosoft.vvecmath.Transform;
import eu.mihosoft.vvecmath.Vector3d;

public abstract class AbstractNode implements SolidNode {
    private Vector3d translate;
    private Vector3d rotate;

    @Override
    public Vector3d getRotate() {
        return rotate;
    }

    @Override
    public void setRotate(Vector3d rotate) {
        this.rotate = rotate;
    }

    @Override
    public Vector3d getTranslate() {
        return translate;
    }

    @Override
    public void setTranslate(Vector3d translate) {
        this.translate = translate;
    }

    protected Solid apply(Solid solid) {
        if (translate == null && rotate == null) {
            return solid;
        }
        var transform = Transform.unity();
        if (translate != null) {
            transform = transform.translate(translate);
        }
        if (rotate != null) {
            transform = transform.rot(rotate);
        }
        return solid.apply(transform);
    }
}
