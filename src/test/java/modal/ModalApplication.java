/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modal;

import com.mamba.mambaui.LoremIpsum;
import com.mamba.mambaui.MambauiTheme;
import com.mamba.mambaui.modal.ModalDialog;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class ModalApplication extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        
        ModalDialog dialog = new ModalDialog();        
        dialog.setHeader("Demo Dialog", LoremIpsum.getLine());
        dialog.setCloseButtonActive(true);
        
        Scene scene = new Scene(new StackPane(dialog), 1000, 700);
                
        stage.setTitle("Close Button Viewer");
        stage.setScene(scene);
        stage.show();
        
        
        dialog.showAndWait();
        
        
    }
}
