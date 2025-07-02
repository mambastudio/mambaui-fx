/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.modal;

import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

/**
 *
 * @author user
 */
public interface ModalDialogBase<T> {
    Optional<T> showAndWait();
    void close();
    
    void setHeader(String title, String description);
    void setHeader(Node graphic, String title, String description);
        
    String getHeaderTitle();
    void setHeaderTitle(String value);
    StringProperty headerTitleProperty();
    
    String getHeaderDescription();
    void setHeaderDescription(String value);
    StringProperty headerDescriptionProperty();
    
    Node getHeaderGraphic();
    void setHeaderGraphic(Node graphic);
    ObjectProperty<Node> headerGraphicProperty();
    
    
    boolean getHeaderCloseButtonActive();
    void setHeaderCloseButtonActive(boolean active);
    BooleanProperty headerCloseButtonActiveProperty();
    
    void setCloseButtonActive(boolean active);
    boolean isVisible();
}
