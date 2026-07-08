/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.modal;

import com.mamba.mambaui.control.Tile;
import module javafx.controls;
import module java.base;
import javafx.scene.control.Label;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 *
 * @author user
 */
public class ModalDialogs {
    private ModalDialogs(){        
    }
    
    public static FontIcon infoIcon()    { return new FontIcon("mdoal-announcement"); }
    public static FontIcon warningIcon() { return new FontIcon("mdsmz-warning"); }
    public static FontIcon errorIcon()   { return new FontIcon("mdral-error_outline"); }
    public static FontIcon helpIcon()    { return new FontIcon("mdral-help_outline"); }
    public static FontIcon successIcon() { return new FontIcon("mdral-done_all"); }
        
    public static ConfirmDialog confirm(String message) {
        var dialog = new ConfirmDialog(message);        
        return dialog;
    }  
    
    public static InformationDialog information(String message) {
        var dialog = new InformationDialog(message);        
        return dialog;
    } 
    
    public static ErrorDialog error() {
        var dialog = new ErrorDialog();        
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

                FontIcon icon = successIcon();
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
                FontIcon icon = infoIcon();                
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
        
        public ErrorDialog() {       
            this("","No error message");
        }
        
        public ErrorDialog(String title, String message) {            
            var messageArea = new TextArea();
            messageArea.setEditable(false);            
            var header = new Tile(title);
            
            super((handle, dialog) -> {
                var ok = new Button("Close");
                ok.setOnAction(e -> handle.cancel());

                var box = new VBox(messageArea);
                box.setPadding(new Insets(0, 0, 5, 0));
                
                VBox.setVgrow(messageArea, Priority.ALWAYS);
                
                var buttonBar = new ButtonBar();
                ButtonBar.setButtonData(ok, ButtonBar.ButtonData.YES);
                buttonBar.getButtons().addAll(ok);

                FontIcon icon = errorIcon();                
                icon.getStyleClass().add("header-help");
                header.setRight(icon);
                
                dialog.setDialogSize(550, 350);
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
        
        public final void setHeader(String text){
            headerTextProperty.setValue(text);
        }
        
        public final void setMessage(String text){
            messageTextProperty.setValue(text);
        } 
        
        public final void setStackTrace(Throwable t) {
            String title = t.getClass().getSimpleName(); // e.g., NullPointerException
            
            String message = t.getMessage();             // e.g., "Variable x was null"
            message += "\n\n";            
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            message += sw.toString();
            
            setHeader(title);
            setMessage(message);
        }
    }
}
