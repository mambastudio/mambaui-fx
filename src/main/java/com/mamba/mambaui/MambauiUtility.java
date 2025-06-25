/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui;

import javafx.scene.control.Button;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 *
 * @author user
 */
public class MambauiUtility {
    public static Button buttonIcon(String iconLiteral, double size,
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
}
