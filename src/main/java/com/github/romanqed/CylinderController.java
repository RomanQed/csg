package com.github.romanqed;

import com.github.romanqed.primitive.Cylinder;
import eu.mihosoft.vvecmath.Vector3d;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static com.github.romanqed.Util.toDouble;

public final class CylinderController extends NodeController {
    private Cylinder cylinder;

    @FXML
    private TextField radiusField;

    @FXML
    private TextField heightField;

    private void update() {
        this.radiusField.setText(Util.toString(cylinder.getStartRadius()));
        this.heightField.setText(Util.toString(cylinder.getEnd().z()));
    }

    @Override
    public void setNode(SolidNode node) {
        this.cylinder = (Cylinder) ((PrimitiveNode) node).getPrimitive();
        this.update();
        super.setNode(node);
    }

    @Override
    protected void onApply() {
        var radius = toDouble(radiusField, 0);
        var height = toDouble(heightField, 0);
        if (radius == null || height == null || radius == 0 || height == 0) {
            Util.showError("Ошибка", "Неверные параметры цилиндра");
            this.update();
            return;
        }
        cylinder.setStart(Vector3d.ZERO);
        cylinder.setEnd(Vector3d.Z_ONE.times(height));
        cylinder.setStartRadius(radius);
        cylinder.setEndRadius(radius);
        super.onApply();
    }
}
