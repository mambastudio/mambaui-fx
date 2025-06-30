/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.modal;

import java.util.Optional;
import javafx.scene.Node;

/**
 *
 * @author user
 */
public interface ModalDialogBase<T> {
    Optional<T> showAndWait();
    void close();
    void setHeader(String title);
    void setHeader(String title, String description);
    void setHeader(Node graphic, String title, String description);
    void setGraphic(Node graphic);
    void setCloseButtonActive(boolean active);
    boolean isVisible();
}
