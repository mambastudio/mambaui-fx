/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.modal;

import java.io.IO;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 *
 * @author user
 * @param <T>
 */
public class ModalDialog<T> extends Control implements ModalDialogBase<T>{
    private static final StyleablePropertyFactory<ModalDialog> FACTORY =
            new StyleablePropertyFactory<>(Control.getClassCssMetaData());
    
    private final ObjectProperty<Node> headerProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Node> contentProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Node> footerProperty = new SimpleObjectProperty<>();
    
    private final DoubleProperty widthDialogProperty = new SimpleDoubleProperty();
    private final DoubleProperty heightDialogProperty = new SimpleDoubleProperty();
    
    private final BooleanProperty triggerDialogProperty = new SimpleBooleanProperty(false);
            
    private Optional<T> result = Optional.empty();
    
    private boolean exited = true;
    
    private static final double VIEW_ORDER_ON_TOP = -1000;
    private static final double VIEW_ORDER_HIDDEN = 1000;
    
    public ModalDialog(BiConsumer<ModalHandle<T>, ModalDialog<T>> dialogBuilder){        
        getStyleClass().add("modal");  
        hide();
                
        //create handle
        ModalHandle<T> handle = new ModalHandle<>() {
            @Override
            public void submit(T r) {
                result = Optional.ofNullable(r);
                close();
            }

            @Override
            public void cancel() {
                result = Optional.empty();
                close();
            }

            @Override
            public void setContent(Node contentNode) {               
                ModalDialog.this.setContent(contentNode);
            }

            @Override
            public void setFooter(Node footerNode) {
                ModalDialog.this.setFooter(footerNode);
            }

            @Override
            public void setHeader(Node header) {
                ModalDialog.this.setHeader(header);
            }
        };

        // Defer until after constructor finishes
        Platform.runLater(() -> dialogBuilder.accept(handle, this));
        
        //add once when modal layer is added to scene graph
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {               
                newScene.windowProperty().addListener((o, ow, nw) -> {
                    if (nw != null) {
                        nw.addEventHandler(javafx.stage.WindowEvent.WINDOW_HIDDEN, e -> {
                            safeExitLoop();
                        });
                    }
                });
            }
        });
    }
    
    @Override protected Skin<?> createDefaultSkin() {return new ModalDialogSkin(this);}
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {return FACTORY.getCssMetaData();}
    @Override public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {return getClassCssMetaData();}
    
    protected ModalDialogSkin getModalSkin(){return (ModalDialogSkin)getSkin();}
    
    @Override
    public String getUserAgentStylesheet() {
        return ModalDialog.class.getResource("modal.css").toExternalForm();
    }
            
    private void show(){
        
        this.setViewOrder(VIEW_ORDER_ON_TOP);
        this.setDisable(false);
        this.toFront(); 
        this.setOpacity(1);
    }
    
    private void hide(){
        this.setOpacity(0);
        this.setViewOrder(VIEW_ORDER_HIDDEN);
        this.setDisable(true);
        this.toBack();
    }
    
    private void safeExitLoop() {
        if (!exited) {
            exited = true;
            Platform.exitNestedEventLoop(this, result);
        }
    }
      
    @Override
    public Optional<T> showAndWait() {
        if(this.getParent() == null)
            throw new UnsupportedOperationException("Current invoked dialog has no parent");
        
        result = Optional.empty();
        exited = false; 
        
        show();        
        return (Optional<T>) Platform.enterNestedEventLoop(this); 
    }
    
    @Override
    public void close() {       
        // Send ModalLayer far to the back and make it fully transparent
        hide();
        safeExitLoop();
    }
        
    @Override
    public final ObjectProperty<Node> headerProperty() {return headerProperty;}
    @Override
    public final void setHeader(Node header) {this.headerProperty.set(header);}
    @Override
    public final Node getHeader() {return headerProperty.get();}
    
    @Override
    public final ObjectProperty<Node> contentProperty() {return contentProperty;}
    @Override
    public final void setContent(Node content) {this.contentProperty.set(content);}
    @Override
    public final Node getContent() {return contentProperty.get();}
    
    @Override
    public final ObjectProperty<Node> footerProperty() {return footerProperty;}
    @Override
    public final void setFooter(Node footer) {this.footerProperty.set(footer);}
    @Override
    public final Node getFooter() {return footerProperty.get();}
    
    
    public final DoubleProperty widthDialogProperty() {return widthDialogProperty;}    
    public final void setWidthDialog(double width) {this.widthDialogProperty.set(width);}    
    public final double getWidthDialog() {return widthDialogProperty.get();}
    
    public final DoubleProperty heightDialogProperty() {return heightDialogProperty;}    
    public final void setHeightDialog(double width) {this.heightDialogProperty.set(width);}    
    public final double getHeightDialog() {return heightDialogProperty.get();}
    
    public final BooleanProperty triggerDialogProperty() {return triggerDialogProperty;}    
    
    public final void setDialogSize(double width, double height){
        setWidthDialog(width);
        setHeightDialog(height);
    }
    
    public interface ModalHandle<T> {
        void submit(T result);
        void cancel();
        void setHeader(Node header);
        void setContent(Node content);
        void setFooter(Node footer);
    }
}
