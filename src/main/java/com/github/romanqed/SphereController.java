package com.github.romanqed;

import com.github.romanqed.primitive.Sphere;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static com.github.romanqed.Util.toDouble;

public final class SphereController extends NodeController {
    private Sphere sphere;

    @FXML
    private TextField radiusField;

    private void update() {
        this.radiusField.setText(Util.toString(sphere.getRadius()));
    }

    @Override
    public void setNode(SolidNode node) {
        this.sphere = (Sphere) ((PrimitiveNode) node).getPrimitive();
        this.update();
        super.setNode(node);
    }

    @Override
    protected void onApply() {
        var radius = toDouble(radiusField, 0);
        if (radius == null || radius == 0) {
            Util.showError("Ошибка", "Неверные параметры сферы");
            this.update();
            return;
        }
        sphere.setRadius(radius);
        super.onApply();
    }
}
