package com.github.romanqed;

import com.github.romanqed.primitive.Cube;
import eu.mihosoft.vvecmath.Vector3d;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static com.github.romanqed.Util.toDouble;

public final class CubeController extends NodeController {
    private Cube cube;

    @FXML
    private TextField widthField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField deepField;

    private void update() {
        var dims = cube.getDimensions();
        this.widthField.setText(Util.toString(dims.x()));
        this.heightField.setText(Util.toString(dims.y()));
        this.deepField.setText(Util.toString(dims.z()));
    }

    @Override
    public void setNode(SolidNode node) {
        this.cube = (Cube) ((PrimitiveNode) node).getPrimitive();
        this.update();
        super.setNode(node);
    }

    @Override
    protected void onApply() {
        var w = toDouble(widthField, 0);
        var h = toDouble(heightField, 0);
        var d = toDouble(heightField, 0);
        if (w == null || h == null || d == null || w == 0 || h == 0 || d == 0) {
            Util.showError("Ошибка", "Неверные параметры параллелепипеда");
            this.update();
            return;
        }
        cube.setDimensions(Vector3d.xyz(w, h, d));
        super.onApply();
    }
}
