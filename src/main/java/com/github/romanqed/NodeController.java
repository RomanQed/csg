package com.github.romanqed;

import eu.mihosoft.vvecmath.Vector3d;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static com.github.romanqed.Util.toDouble;

public class NodeController implements Initializable {
    @FXML
    private TextField xField;
    @FXML
    private TextField yField;
    @FXML
    private TextField zField;
    @FXML
    private TextField xRField;
    @FXML
    private TextField yRField;
    @FXML
    private TextField zRField;

    private SolidNode node;
    private Runnable remover;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        xField.setText("0");
        yField.setText("0");
        zField.setText("0");
        xRField.setText("0");
        yRField.setText("0");
        zRField.setText("0");
    }

    private void update() {
        var translate = node.getTranslate();
        var rotate = node.getRotate();
        if (translate != null) {
            xField.setText(Util.toString(translate.x()));
            yField.setText(Util.toString(translate.y()));
            zField.setText(Util.toString(translate.z()));
        }
        if (rotate != null) {
            xRField.setText(Util.toString(rotate.x()));
            yRField.setText(Util.toString(rotate.y()));
            zRField.setText(Util.toString(rotate.z()));
        }
    }

    public void setNode(SolidNode node) {
        this.node = node;
        this.update();
    }

    public void setRemover(Runnable remover) {
        this.remover = remover;
    }

    @FXML
    protected void onApply() {
        var x = toDouble(xField);
        var y = toDouble(yField);
        var z = toDouble(zField);
        if (x == null || y == null || z == null) {
            Util.showError("Ошибка", "Некорректные значения смещения");
            this.update();
            return;
        }
        var xR = toDouble(xRField);
        var yR = toDouble(yRField);
        var zR = toDouble(zRField);
        if (xR == null || yR == null || zR == null) {
            Util.showError("Ошибка", "Некорректные значения поворота");
            this.update();
            return;
        }
        node.setTranslate(Vector3d.xyz(x, y, z));
        node.setRotate(Vector3d.xyz(xR, yR, zR));
    }

    @FXML
    private void onRemove() {
        remover.run();
    }
}
