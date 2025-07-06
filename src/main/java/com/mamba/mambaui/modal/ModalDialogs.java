/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.modal;

import com.mamba.mambaui.control.Tile;
import java.util.function.Consumer;
import javafx.scene.control.ButtonBar;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 *
 * @author user
 */
public class ModalDialogs {
    private ModalDialogs(){        
    }
        
    public static ConfirmDialog confirm(String message) {
        var dialog = new ConfirmDialog(message);        
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
                header.getStyleClass().add("tile");

                FontIcon icon = new FontIcon("mdral-help_outline");
                icon.setIconSize(60);
                header.setRight(icon);
                
                dialog.setDialogSize(350, 150);

                handle.setHeader(header);
                handle.setFooter(buttonBar);
            });            
        }        
    }
}
