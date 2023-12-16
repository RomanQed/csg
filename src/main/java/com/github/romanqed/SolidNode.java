package com.github.romanqed;

import com.github.romanqed.csg.Solid;
import com.github.romanqed.csg.SolidFactory;
import eu.mihosoft.vvecmath.Vector3d;

public interface SolidNode {
    Vector3d getTranslate();

    void setTranslate(Vector3d translate);

    Vector3d getRotate();

    void setRotate(Vector3d rotate);

    Solid toSolid(SolidFactory factory);
}
