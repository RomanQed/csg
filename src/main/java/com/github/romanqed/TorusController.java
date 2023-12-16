package com.github.romanqed;

import com.github.romanqed.primitive.Torus;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static com.github.romanqed.Util.toDouble;

public final class TorusController extends NodeController {
    private Torus torus;

    @FXML
    private TextField centerRadiusField;

    @FXML
    private TextField innerRadiusField;

    private void update() {
        this.centerRadiusField.setText(Util.toString(torus.getCenterRadius()));
        this.innerRadiusField.setText(Util.toString(torus.getInnerRadius()));
    }

    @Override
    public void setNode(SolidNode node) {
        this.torus = (Torus) ((PrimitiveNode) node).getPrimitive();
        this.update();
        super.setNode(node);
    }

    @Override
    protected void onApply() {
        var centerRadius = toDouble(centerRadiusField, 0);
        var innerRadius = toDouble(innerRadiusField, 0);
        if (centerRadius == null
                || innerRadius == null
                || centerRadius == 0
                || innerRadius == 0
                || innerRadius >= centerRadius) {
            Util.showError("Ошибка", "Неверные параметры тора");
            this.update();
            return;
        }
        torus.setCenterRadius(centerRadius);
        torus.setInnerRadius(innerRadius);
        super.onApply();
    }
}
