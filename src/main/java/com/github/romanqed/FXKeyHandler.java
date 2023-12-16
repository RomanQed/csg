package com.github.romanqed;

import com.github.romanqed.bindings.AbstractKeyHandler;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;

public final class FXKeyHandler extends AbstractKeyHandler<KeyEvent> implements EventHandler<KeyEvent> {
    private final Node node;

    public FXKeyHandler(Node node) {
        this.node = node;
    }

    @Override
    public void start() {
        node.addEventHandler(KeyEvent.KEY_PRESSED, this);
    }

    @Override
    public void stop() {
        node.removeEventHandler(KeyEvent.KEY_PRESSED, this);
    }

    @Override
    public void handle(KeyEvent event) {
        this.handle(event.getCode().getCode(), event);
    }
}
