package com.github.romanqed;

import com.github.romanqed.bindings.KeyHandler;
import com.github.romanqed.csg.BSPSolidFactory;
import com.github.romanqed.csg.SolidFactory;
import com.github.romanqed.graphic.AreaRasterizerFactory;
import com.github.romanqed.graphic.ParallelRendererFactory;
import com.github.romanqed.graphic.RasterizerFactory;
import com.github.romanqed.graphic.RendererFactory;
import com.github.romanqed.json.JsonNodeSerializer;
import com.github.romanqed.primitive.Cube;
import com.github.romanqed.primitive.Cylinder;
import com.github.romanqed.primitive.Sphere;
import com.github.romanqed.primitive.Torus;
import com.github.romanqed.scene.Camera;
import com.github.romanqed.scene.PerspectiveProjection;
import com.github.romanqed.scene.Projection;
import com.github.romanqed.scene.Scene;
import com.github.romanqed.swing.SwingScene;
import com.github.romanqed.util.ConvertUtil;
import com.github.romanqed.util.MapStorage;
import eu.mihosoft.vvecmath.Vector3d;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import static com.github.romanqed.Util.toDouble;

public final class MainController implements Initializable {
    private static final double FOV = Math.toRadians(45);
    private static final double NEAR = 0.001;
    private static final double FAR = 100;
    private static final Projection PROJECTION = new PerspectiveProjection(FOV, NEAR, FAR);
    private static final RasterizerFactory RASTERIZER_FACTORY = new AreaRasterizerFactory();
    private static final RendererFactory RENDERER_FACTORY = new ParallelRendererFactory();
    private static final SolidFactory SOLID_FACTORY = new BSPSolidFactory();
    private static final double MOVE_SPEED = 0.1;
    private static final double ROTATE_SPEED = 1;
    private static final NodeSerializer SERIALIZER = new JsonNodeSerializer();

    public VBox sceneBox;
    public ComboBox<String> addOp;

    @FXML
    private BorderPane root;
    @FXML
    private SwingNode node;
    private Scene scene;
    private Camera camera;
    private FlatShader shader;
    private SwingScene swingScene;
    private KeyHandler<KeyEvent> handler;
    private Stage stage;
    // Camera fields
    @FXML
    private TextField cameraX;
    @FXML
    private TextField cameraY;
    @FXML
    private TextField cameraZ;
    @FXML
    private TextField cameraYaw;
    @FXML
    private TextField cameraPitch;

    private List<SolidNode> nodes;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setup nodes list
        this.nodes = new LinkedList<>();
        // Setup camera
        this.camera = new Camera(Vector3d.ZERO, Vector3d.Y_ONE);
        this.setupCameraInputs();
        // Setup shader
        this.shader = new FlatShader(camera.getFront().negated());
        // Setup scene
        this.scene = new Scene(camera, PROJECTION);
        // Setup swing scene
        this.swingScene = new SwingScene(RASTERIZER_FACTORY, RENDERER_FACTORY, this.shader);
        this.swingScene.setColor(Color.BLACK);
        this.swingScene.setScene(scene);
        // Setup key handler
        this.handler = new FXKeyHandler(root);
        this.setupKeys();
        this.handler.start();
        SwingUtilities.invokeLater(() -> node.setContent(swingScene));
    }

    private void setupCameraInputs() {
        var position = camera.getPosition();
        cameraX.setText(Util.toString(position.x()));
        cameraY.setText(Util.toString(position.y()));
        cameraZ.setText(Util.toString(position.z()));
        cameraYaw.setText(Util.toString(camera.getYaw()));
        cameraPitch.setText(Util.toString(camera.getPitch()));
    }

    private void setupKeys() {
        // Movements
        // X-axis
        handler.register(KeyCode.A.getCode(), "LEFT", () -> moveX(-MOVE_SPEED));
        handler.register(KeyCode.D.getCode(), "RIGHT", () -> moveX(MOVE_SPEED));
        // Z-axis
        handler.register(KeyCode.W.getCode(), "BACK", () -> moveZ(MOVE_SPEED));
        handler.register(KeyCode.S.getCode(), "FRONT", () -> moveZ(-MOVE_SPEED));
        // Rotations
        // Yaw
        handler.register(KeyCode.Q.getCode(), "ROT_LEFT", () -> rotateYaw(-ROTATE_SPEED));
        handler.register(KeyCode.E.getCode(), "ROT_RIGHT", () -> rotateYaw(ROTATE_SPEED));
        // Pitch
        handler.register(KeyCode.Z.getCode(), "ROT_DOWN", () -> rotatePitch(-ROTATE_SPEED));
        handler.register(KeyCode.X.getCode(), "ROT_UP", () -> rotatePitch(ROTATE_SPEED));
    }

    private void moveX(double offset) {
        camera.moveX(offset);
        this.setupCameraInputs();
        swingScene.repaint();
    }

    private void moveZ(double offset) {
        camera.moveZ(offset);
        this.setupCameraInputs();
        swingScene.repaint();
    }

    private void rotateYaw(double offset) {
        camera.rotateYaw(offset);
        this.setupCameraInputs();
        updateShader();
        swingScene.repaint();
    }

    private void rotatePitch(double offset) {
        camera.rotatePitch(offset);
        this.setupCameraInputs();
        updateShader();
        swingScene.repaint();
    }

    private void updateShader() {
        this.shader.setNormal(this.camera.getFront().negated());
    }

    @FXML
    private void onSetCamera() {
        var x = toDouble(cameraX);
        var y = toDouble(cameraY);
        var z = toDouble(cameraZ);
        if (x == null || y == null || z == null) {
            Util.showError("Ошибка", "Неверная позиция камеры");
            return;
        }
        var yaw = toDouble(cameraYaw, 0, 359);
        var pitch = toDouble(cameraPitch, 0, 359);
        if (yaw == null || pitch == null) {
            Util.showError("Ошибка", "Неверные углы камеры, допустимые значения угла от 0 до 359");
            return;
        }
        var position = Vector3d.xyz(x, y, z);
        camera.setPosition(position);
        camera.setYaw(yaw);
        camera.setPitch(pitch);
        updateShader();
        swingScene.repaint();
    }

    @FXML
    private void onExitAction() {
        this.handler.stop();
        System.exit(0);
    }

    @FXML
    private void onAboutAction() throws IOException {
        Util.showInfo("О программе", Util.readResourceFile("about.txt"));
    }

    @FXML
    private void onAuthorAction() {
        Util.showInfo("Автор", "Бакалдин Роман ИУ7-55Б");
    }

    @FXML
    private void onAddNode() throws IOException {
        var value = addOp.getSelectionModel().getSelectedItem();
        var node = (TitledPane) SceneUtil.loadNodeByName(getClass(), value);
        if (node == null) {
            return;
        }
        node.setExpanded(false);
        var controller = (NodeController) node.getUserData();
        var solidNode = SceneUtil.createSolidNode(value);
        nodes.add(solidNode);
        controller.setNode(solidNode);
        controller.setRemover(() -> {
            nodes.remove(solidNode);
            sceneBox.getChildren().remove(node);
        });
        sceneBox.getChildren().add(node);
    }

    @FXML
    private void onRender() {
        var objects = scene.getObjects();
        objects.clear();
        var flag = false;
        for (var node : nodes) {
            var solid = node.toSolid(SOLID_FACTORY);
            if (solid == null) {
                flag = true;
                continue;
            }
            var storage = solid.getStorage();
            if (storage == null) {
                storage = new MapStorage();
                solid.setStorage(storage);
            }
            storage.set("color", Color.GREEN);
            var object = ConvertUtil.solidToModel(solid);
            objects.add(object);
        }
        swingScene.repaint();
        if (flag) {
            Util.showInfo("Предупреждение", "Некоторые объекты не были отрендерены");
        }
    }

    private File chooseFile(String title, boolean open) throws IOException {
        var chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File(".").getCanonicalFile());
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Scene file", "*.scene"));
        if (open) {
            return chooser.showOpenDialog(stage);
        }
        return chooser.showSaveDialog(stage);
    }

    private void save(File file, List<SolidNode> nodes) throws IOException {
        var output = new FileOutputStream(file);
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        for (var node : nodes) {
            writer.write(SERIALIZER.serialize(node) + "\n");
        }
        writer.close();
        output.close();
    }

    @FXML
    private void onSave() throws IOException {
        var file = chooseFile("Сохранить файл сцены", false);
        if (file == null) {
            return;
        }
        save(file, nodes);
    }

    private Node makePrimitiveNode(PrimitiveNode node) throws IOException {
        var primitive = node.getPrimitive();
        var clazz = primitive.getClass();
        Node ret = null;
        if (clazz == Cube.class) {
            ret = SceneUtil.loadNode(getClass(), "cube.fxml");
        }
        if (clazz == Cylinder.class) {
            ret = SceneUtil.loadNode(getClass(), "cylinder.fxml");
        }
        if (clazz == Sphere.class) {
            ret = SceneUtil.loadNode(getClass(), "sphere.fxml");
        }
        if (clazz == Torus.class) {
            ret = SceneUtil.loadNode(getClass(), "torus.fxml");
        }
        if (ret == null) {
            throw new IllegalArgumentException();
        }
        var controller = (NodeController) ret.getUserData();
        controller.setNode(node);
        return ret;
    }

    private Node makeNode(SolidNode node) throws IOException {
        if (node instanceof PrimitiveNode) {
            var ret = makePrimitiveNode((PrimitiveNode) node);
            ((NodeController) ret.getUserData()).setRemover(() -> {
                nodes.remove(node);
                sceneBox.getChildren().remove(ret);
            });
            return ret;
        }
        var ret = SceneUtil.loadNode(getClass(), "operation.fxml");
        var controller = (MergeController) ret.getUserData();
        var merge = (MergeNode) node;
        controller.setRemover(() -> {
            nodes.remove(node);
            sceneBox.getChildren().remove(ret);
        });
        controller.setNode(node);
        controller.setFirst(makeNode(merge.getFirst()));
        controller.setSecond(makeNode(merge.getSecond()));
        return ret;
    }

    private void clearSceneUI() {
        var children = sceneBox.getChildren();
        var temp = children.get(0);
        children.clear();
        children.add(temp);
    }

    private void rebuildSceneUI() throws IOException {
        // Clear
        clearSceneUI();
        // Add nodes
        var children = sceneBox.getChildren();
        for (var node : nodes) {
            var element = makeNode(node);
            children.add(element);
        }
    }

    private List<SolidNode> load(File file) throws IOException {
        var ret = new LinkedList<SolidNode>();
        var input = new FileInputStream(file);
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        reader.lines().forEach(line -> {
            if (line.isEmpty()) {
                return;
            }
            ret.add(SERIALIZER.deserialize(line));
        });
        reader.close();
        input.close();
        return ret;
    }

    @FXML
    private void onLoad() throws IOException {
        var file = chooseFile("Открыть файл сцены", true);
        if (file == null) {
            return;
        }
        List<SolidNode> loaded;
        try {
            loaded = load(file);
        } catch (Exception e) {
            Util.showError("Ошибка загрузки", "Некорректный файл сцены");
            return;
        }
        nodes = loaded;
        rebuildSceneUI();
        onRender();
    }

    @FXML
    private void onClear() {
        clearSceneUI();
        nodes.clear();
        scene.getObjects().clear();
        swingScene.repaint();
    }
}
