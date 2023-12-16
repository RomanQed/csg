package com.github.romanqed;

import com.github.romanqed.primitive.Cube;
import com.github.romanqed.primitive.Cylinder;
import com.github.romanqed.primitive.Sphere;
import com.github.romanqed.primitive.Torus;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.Map;

final class SceneUtil {
    static final Map<String, String> SCENES = Map.of(
            "Куб", "cube.fxml",
            "Цилиндр", "cylinder.fxml",
            "Сфера", "sphere.fxml",
            "Тор", "torus.fxml",
            "Операция", "operation.fxml"
    );

    private SceneUtil() {
    }

    static Node loadNode(Class<?> clazz, String path) throws IOException {
        var loader = new FXMLLoader(clazz.getResource(path));
        return loader.load();
    }

    static Node loadNodeByName(Class<?> clazz, String value) throws IOException {
        var scene = SCENES.get(value);
        if (scene == null) {
            return null;
        }
        return loadNode(clazz, scene);
    }

    static SolidNode createSolidNode(String value) {
        switch (value) {
            case "Куб":
                return new PrimitiveNode(new Cube(1));
            case "Цилиндр":
                return new PrimitiveNode(new Cylinder(0.5, 1));
            case "Сфера":
                return new PrimitiveNode(new Sphere(0.5));
            case "Тор":
                return new PrimitiveNode(new Torus(1, 0.5));
            case "Операция":
                return new MergeNode();
            default:
                throw new IllegalArgumentException("Unknown node value");
        }
    }
}
