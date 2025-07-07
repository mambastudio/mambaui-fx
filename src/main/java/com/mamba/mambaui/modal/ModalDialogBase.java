/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.modal;

import java.util.Optional;
import java.util.function.Consumer;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;

/**
 *
 * @author user
 * @param <T>
 */
public interface ModalDialogBase<T> {
    Optional<T> showAndWait();
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
