/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.modal;

import module javafx.controls;
import module java.base;
/**
 *
 * @author user
 * @param <T>
 */
public interface ModalDialogBase<T> {
    
    default void showAndWait(Consumer<Optional<T>> onResult) {
        Platform.runLater(() -> onResult.accept(displayAndReturn()));
    }
    Optional<T> displayAndReturn();
    void close();
        
    ObjectProperty<Node> headerProperty();
    void setHeader(Node content);
    Node getHeader();
    
    ObjectProperty<Node> contentProperty();
    void setContent(Node content);
    Node getContent();
    
    ObjectProperty<Node> footerProperty();
    void setFooter(Node content);
    Node getFooter();
    
    boolean isVisible();
}
