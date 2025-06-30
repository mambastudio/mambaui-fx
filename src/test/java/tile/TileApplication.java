/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tile;


import com.mamba.mambaui.LoremIpsum;
import com.mamba.mambaui.MambauiTheme;
import com.mamba.mambaui.MambauiUtility;
import com.mamba.mambaui.control.Tile;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class TileApplication  extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        
        Tile tile = new Tile("JOSTO", LoremIpsum.getLine());
        tile.setRight(new Button("X"));
        tile.setPrefHeight(80);
        tile.setPadding(new Insets(5));
        
        Scene scene = new Scene(new StackPane(tile), 200, 200);
        
        MambauiTheme.applyTo(scene);
        
        stage.setTitle("Tile");
        stage.setScene(scene);
        stage.show();
    }
    
    
    
}
