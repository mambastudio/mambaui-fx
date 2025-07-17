/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.modal;

import com.mamba.mambaui.control.Tile;
import java.io.IO;
import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
            var label = new Label();
            label.setWrapText(true);
            
            super((handle, dialog) -> {
                var ok = new Button("OK");
                ok.setOnAction(e -> handle.cancel());

                var box = new VBox(label);
                
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
            setMessage(message);
            label.textProperty().bind(messageText);            
        } 
        
        public final void setMessage(String text){
            messageText.set(text);
        }        
    }
    
    public static class ErrorDialog extends ModalDialog<Void> {
        private final StringProperty headerTextProperty = new SimpleStringProperty();
        private final StringProperty messageTextProperty = new SimpleStringProperty();
        
        public ErrorDialog(String title, String message) {            
            var messageArea = new TextArea();
            messageArea.setEditable(false);            
            var header = new Tile(title);
            
            super((handle, dialog) -> {
                var ok = new Button("Close");
                ok.setOnAction(e -> handle.cancel());

                var box = new VBox(messageArea);
                
                var buttonBar = new ButtonBar();
                ButtonBar.setButtonData(ok, ButtonBar.ButtonData.YES);
                buttonBar.getButtons().addAll(ok);

                FontIcon icon = error();                
                icon.getStyleClass().add("header-help");
                header.setRight(icon);
                
                dialog.setDialogSize(350, 150);
                handle.setHeader(header);
                handle.setContent(box);
                handle.setFooter(buttonBar);                
            });
            
            setHeader(header);
            header.headerProperty().bind(headerTextProperty);   
            
            setMessage(message);
            messageArea.textProperty().bind(messageTextProperty);            
        } 
        
        public ErrorDialog(Throwable error) {
            String title = error.getClass().getSimpleName(); // e.g., NullPointerException
            String message = error.getMessage();             // e.g., "Variable x was null"
            message += "\n\n";
            
            StringWriter sw = new StringWriter();
            error.printStackTrace(new PrintWriter(sw));
            message += sw.toString();
            
            this(title, message);
        }
        
        private void setHeader(String text){
            headerTextProperty.setValue(text);
        }
        
        private void setMessage(String text){
            messageTextProperty.setValue(text);
        } 
        
        private String getStackTrace(Throwable t) {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
    }
}
