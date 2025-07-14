/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modal;

import com.mamba.mambaui.MambauiTheme;
import com.mamba.mambaui.modal.ModalDialogs;
import java.io.IO;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class ModalApplication extends Application{
    @Override
    public void start(Stage stage) throws Exception {
        
        var dialog = ModalDialogs.information("Are you sure you want to proceed?");
        
        Button btn = new Button("Open");
        btn.setOnAction(e-> {            
            var result = dialog.showAndWait();               
                
            switch(result.isPresent()){
                case true -> IO.println(result.get());
                case false -> IO.println("cancelled");
            }
        });
        
        Scene scene = new Scene(new StackPane(dialog, btn), 1000, 700);
        
        MambauiTheme.applyTo(scene);
                
        stage.setTitle("Close Button Viewer");
        stage.setScene(scene);
        stage.show(); 
    }
    
}
