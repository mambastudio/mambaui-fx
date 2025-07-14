/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.modal;

import com.mamba.mambaui.control.Tile;
import java.io.IO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 *
 * @author user
 */
public class ModalDialogs {
    private ModalDialogs(){        
    }
    
    public static FontIcon info()    { return new FontIcon("mdoal-announcement"); }
    public static FontIcon warning() { return new FontIcon("mdsmz-warning"); }
    public static FontIcon error()   { return new FontIcon("mdral-error_outline"); }
    public static FontIcon help()    { return new FontIcon("mdral-help_outline"); }
    public static FontIcon success() { return new FontIcon("mdral-done_all"); }
        
    public static ConfirmDialog confirm(String message) {
        var dialog = new ConfirmDialog(message);        
        return dialog;
    }  
    
    public static InformationDialog information(String message) {
        var dialog = new InformationDialog(message);        
        return dialog;
    }  
    
    public static class ConfirmDialog extends ModalDialog<Boolean> {
        public ConfirmDialog(String message) {
            super((handle, dialog) -> {
                var ok = new javafx.scene.control.Button("OK");
                var cancel = new javafx.scene.control.Button("Cancel");
                ok.setOnAction(e -> handle.submit(true));
                cancel.setOnAction(e -> handle.cancel());

                var buttonBar = new ButtonBar();
                ButtonBar.setButtonData(ok, ButtonBar.ButtonData.YES);
                ButtonBar.setButtonData(cancel, ButtonBar.ButtonData.NO);
                buttonBar.getButtons().addAll(ok, cancel);

                Tile header = new Tile("Confirmation", message);                

                FontIcon icon = success();
                icon.getStyleClass().add("header-help");
                header.setRight(icon);
                
                dialog.setDialogSize(350, 150);

                handle.setHeader(header);
                handle.setFooter(buttonBar);
            });            
        }        
    }
    
    public static class InformationDialog extends ModalDialog<Void> {
        private final StringProperty messageText = new SimpleStringProperty();

        public InformationDialog(String message) {
            var label = new Label(message);
            label.setWrapText(true);
            
            super((handle, dialog) -> {
                var ok = new Button("OK");
                ok.setOnAction(e -> handle.cancel());

                var box = new VBox(label);
                handle.setContent(box);

                var buttonBar = new ButtonBar();
                ButtonBar.setButtonData(ok, ButtonBar.ButtonData.YES);
                buttonBar.getButtons().addAll(ok);

                Tile header = new Tile("Information");
                FontIcon icon = info();                
                icon.getStyleClass().add("header-help");
                header.setRight(icon);
                
                dialog.setDialogSize(350, 150);
                handle.setHeader(header);
                handle.setContent(box);
                handle.setFooter(buttonBar);                
            });
            
            label.textProperty().bind(messageText);            
        } 
        
        public void setMessage(String text){
            messageText.set(text);
        }        
    }
}
