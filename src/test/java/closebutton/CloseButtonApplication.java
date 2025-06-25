/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package closebutton;


import com.mamba.mambaui.MambauiTheme;
import com.mamba.mambaui.MambauiUtility;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class CloseButtonApplication  extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        
        Button closeBtn = MambauiUtility.buttonIcon("mdal-close", 30, "circular-button", "close-icon");
        
        Scene scene = new Scene(new StackPane(closeBtn), 200, 200);
        
        MambauiTheme.applyTo(scene);
        
        stage.setTitle("Close Button Viewer");
        stage.setScene(scene);
        stage.show();
    }
    
    
    
}
