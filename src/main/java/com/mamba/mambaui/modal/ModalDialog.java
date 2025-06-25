/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.modal;

import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
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
public class ModalDialog<T> extends Control{
    private static final StyleablePropertyFactory<ModalDialog> FACTORY =
            new StyleablePropertyFactory<>(Control.getClassCssMetaData());
    
    private final ObjectProperty<Node> content = new SimpleObjectProperty<>();
   // private Optional<ModalContent<T>> modalContent = Optional.empty();    
    private Optional<T> result = Optional.empty();
    
    private boolean exited = true;
        
    public ModalDialog(){        
        getStyleClass().add("modal-layer");  
        hide();
        
        content.addListener((o, ov, nv)->{
            if(nv != null)
                getModalSkin().getBorderPane().setCenter(nv);
        });
        
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
    
    // Optional if using custom stylesheet
    @Override
    public String getUserAgentStylesheet() {
        return getClass().getResource("modaldialog.css").toExternalForm();
    }
        
  

    public Node getContent() {
        return content.get();
    }

    public ObjectProperty<Node> contentProperty() {
        return content;
    }    

    // When content (or user action) builds a result
    public void setResult(T result) {
        this.result = Optional.ofNullable(result);
    }
    
    private void show(){
        this.setOpacity(1);
        this.setViewOrder(-1000);
        this.setDisable(false);
        
    }
    
    private void hide(){
        this.setOpacity(0);
        this.setViewOrder(1000);
        this.setDisable(true);
    }
    
    private void safeExitLoop() {
        if (!exited) {
            exited = true;
            Platform.exitNestedEventLoop(this, null);
        }
    }
    
    public Optional<T> showAndWait() {
        exited = false;
        // Bring ModalLayer to the top and make it visible
        // Add in platform run later to ensure it doesn't glitch during first attempt in resizing.
        Platform.runLater(()->{
            show();        
            // Enter nested event loop to wait for close()  
            Platform.enterNestedEventLoop(this);           
        });
       
        // After closing, build the result
        return result;      
    }
    
    public void close() {
       // if (modalContent.isPresent()) {
       //     this.result = modalContent.get().buildResult();
       // }
        
        // Send ModalLayer far to the back and make it fully transparent
        hide();
        safeExitLoop();
    }
}
