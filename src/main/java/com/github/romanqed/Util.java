package com.github.romanqed;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

final class Util {
    private Util() {
    }

    static String readInputStream(InputStream stream, Charset charset) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
        return reader.lines().reduce("", (left, right) -> left + right + "\n");
    }

    static String readResourceFile(String name, Charset charset) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream stream = classLoader.getResourceAsStream(name);
        if (stream == null) {
            throw new IOException();
        }
        String ret = readInputStream(stream, charset);
        stream.close();
        return ret;
    }

    static String readResourceFile(String name) throws IOException {
        return readResourceFile(name, StandardCharsets.UTF_8);
    }

    static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    static String toString(double value) {
        return String.format(Locale.US, "%.4f", value);
    }

    static Double toDouble(TextField field, double min, double max) {
        try {
            var value = Double.parseDouble(field.getText());
            if (value < min || value > max) {
                return null;
            }
            return value;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static Double toDouble(TextField field, double min) {
        return toDouble(field, min, Double.POSITIVE_INFINITY);
    }

    static Double toDouble(TextField field) {
        return toDouble(field, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }
}
