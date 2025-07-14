/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui;

import java.util.Objects;
import javafx.scene.Scene;

/**
 *
 * @author user
 */
public final class MambauiTheme {
    private MambauiTheme() {}

    public static final String SHEET = 
        MambauiTheme.class.getResource("theme/mambaui-theme.css").toExternalForm();

    public static void applyTo(Scene scene, String... overrides) {
        if (!scene.getStylesheets().contains(SHEET)) {
            scene.getStylesheets().add(SHEET);
        }

        for (String override : overrides) {
            if (!scene.getStylesheets().contains(override)) {
                scene.getStylesheets().add(override);
            }
        }
    }
    
    public static void applyTo(Class clazz, Scene scene, String baseTheme) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(scene);
        Objects.requireNonNull(baseTheme);
        
        String theme = clazz.getResource(baseTheme).toExternalForm();
        
        if (!scene.getStylesheets().contains(theme)) {
            scene.getStylesheets().add(theme);
        }
    }

    public static void switchTo(Scene scene, String baseTheme, String... overrides) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(baseTheme);
        for (String override : overrides) {
            if (!scene.getStylesheets().contains(override)) {
                scene.getStylesheets().add(override);
            }
        }
    }
}
