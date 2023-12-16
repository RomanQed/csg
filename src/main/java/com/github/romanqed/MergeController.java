package com.github.romanqed;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.function.Consumer;

public final class MergeController extends NodeController {
    @FXML
    private ComboBox<String> firstAdd;
    @FXML
    private ComboBox<String> secondAdd;
    @FXML
    private ComboBox<String> mergeOp;
    @FXML
    private VBox firstBox;
    @FXML
    private VBox secondBox;
    private MergeNode node;

    @Override
    public void setNode(SolidNode node) {
        this.node = (MergeNode) node;
        var operation = this.node.getOperation();
        if (operation != null) {
            switch (operation) {
                case DIFFERENCE:
                    mergeOp.getSelectionModel().select(1);
                    break;
                case UNION:
                    mergeOp.getSelectionModel().select(0);
                    break;
                case INTERSECTION:
                    mergeOp.getSelectionModel().select(2);
                    break;
            }
        }
        super.setNode(node);
    }

    private void set(VBox box, Node node, Runnable onRemove) {
        var buffer = box.getChildren().get(1);
        var controller = (NodeController) node.getUserData();
        controller.setRemover(() -> {
            onRemove.run();
            box.getChildren().set(1, buffer);
        });
        box.getChildren().set(1, node);
    }

    public void setFirst(Node node) {
        set(firstBox, node, () -> this.node.setFirst(null));
    }

    public void setSecond(Node node) {
        set(secondBox, node, () -> this.node.setSecond(null));
    }

    private void addNode(VBox box, String value, Consumer<SolidNode> onAdd, Runnable onRemove)
            throws IOException {
        var toAdd = (TitledPane) SceneUtil.loadNodeByName(getClass(), value);
        if (toAdd == null) {
            return;
        }
        toAdd.setExpanded(false);
        var controller = (NodeController) toAdd.getUserData();
        var solidNode = SceneUtil.createSolidNode(value);
        controller.setNode(solidNode);
        onAdd.accept(solidNode);
        var buffer = box.getChildren().get(1);
        controller.setRemover(() -> {
            onRemove.run();
            box.getChildren().set(1, buffer);
        });
        box.getChildren().set(1, toAdd);
    }

    @FXML
    private void onFirstSet() throws IOException {
        addNode(firstBox,
                firstAdd.getSelectionModel().getSelectedItem(),
                e -> node.setFirst(e),
                () -> node.setFirst(null));
    }

    @FXML
    private void onSecondSet() throws IOException {
        addNode(secondBox,
                secondAdd.getSelectionModel().getSelectedItem(),
                e -> node.setSecond(e),
                () -> node.setSecond(null));
    }

    @Override
    protected void onApply() {
        switch (mergeOp.getSelectionModel().getSelectedItem()) {
            case "Объединение":
                node.setOperation(SolidOperation.UNION);
                break;
            case "Вычитание":
                node.setOperation(SolidOperation.DIFFERENCE);
                break;
            case "Пересечение":
                node.setOperation(SolidOperation.INTERSECTION);
                break;
            default:
                Util.showError("Ошибка", "Выберите операцию");
                return;
        }
        super.onApply();
    }
}
