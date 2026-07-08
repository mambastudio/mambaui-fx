/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.icon;

import javafx.scene.control.Button;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 *
 * @author user
 */
public final class IconButtons {
    private IconButtons() {
    }

    public static Button fontIconButton(String iconLiteral, double size,
                                        String buttonClass, String iconClass) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.setIconSize((int) size);
        icon.getStyleClass().add(iconClass); // e.g., "close-icon"

        Button btn = new Button();
        btn.setPrefSize(size, size);
        btn.setGraphic(icon);
        btn.getStyleClass().add(buttonClass); // e.g., "circular-button"
        return btn;
    }

    public static Button buttonIcon(String iconLiteral, double size,
                                    String buttonClass, String iconClass) {
        return fontIconButton(iconLiteral, size, buttonClass, iconClass);
    }
}
