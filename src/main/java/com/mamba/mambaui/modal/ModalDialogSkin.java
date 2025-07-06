/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.modal;

import com.mamba.mambaui.MambauiUtility;
import com.mamba.mambaui.control.Tile;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author user
 */
public final class ModalDialogSkin extends SkinBase<ModalDialog> implements Skin<ModalDialog> {    
    private enum ResizeMode {
        NONE, EAST, WEST, NORTH, SOUTH,
        NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST
    }
    
    private static final int RESIZE_MARGIN = 4;
    private ResizeMode currentResize = ResizeMode.NONE;
        
    private double pressCenterX = 0;
    private double pressCenterY = 0;
    
    private double edgeOffsetX, edgeOffsetY;
        
    protected final StackPane root;
    protected final BorderPane dialogPane = new BorderPane();
    protected Tile header = new Tile();
    protected Button closeBtn = MambauiUtility.buttonIcon("mdal-close", 30, "circular-button", "close-icon");
    
    public ModalDialogSkin(ModalDialog dialog) {
        super(dialog); 
                
        dialogPane.setTop(dialog.getHeader());
        dialogPane.setCenter(dialog.getContent());
        dialogPane.setBottom(dialog.getFooter());
        setDialogSize(dialog.getWidthDialog(), dialog.getHeightDialog());

        dialog.headerProperty().addListener((_, _, newVal)->{
            dialogPane.setTop((Node) newVal);
        });
        dialog.contentProperty().addListener((_, _, newVal)->{
            dialogPane.setCenter((Node) newVal);
        });
        dialog.footerProperty().addListener((_, _, newVal)->{
            dialogPane.setBottom((Node) newVal);
        });
        dialog.widthDialogProperty().addListener((_, _, newVal)->{
            setDialogWidth((double) newVal);
        });
        dialog.heightDialogProperty().addListener((_, _, newVal)->{
            setDialogHeight((double) newVal);
        });
            
        root = new StackPane();        
        initGraphics();        
    }
    
    private void initGraphics(){
        this.dialogPane.getStyleClass().add("pane"); 
        header.setPrefHeight(90);
       // dialogPane.setTop(header);
        
        root.getChildren().add(dialogPane); //add pane first to root stack
        getChildren().add(root); //add root to skin
                
        root.setOnMouseMoved(this::handleMouseMoved);
        root.setOnMousePressed(this::handleMousePressed);
        root.setOnMouseDragged(this::handleMouseDragged);
             
        //Re-center automatically when parent resizes
        dialogPane.parentProperty().addListener((obs, oldParent, newParent) -> {
            if (newParent instanceof Pane parent) {
                parent.widthProperty().addListener((o, ov, nv) -> dialogPane.requestLayout());
                parent.heightProperty().addListener((o, ov, nv) -> dialogPane.requestLayout());
            }
        });
        
        closeBtn.setOnMouseEntered(e-> closeBtn.setCursor(Cursor.DEFAULT));
        closeBtn.setOnAction(e->{
             ((ModalDialog<?>) getSkinnable()).close();
        });
       
        header.setPadding(new Insets(5));        
        header.getStyleClass().add("tile");
        
        this.dialogPane.setPadding(new Insets(5));
        
        //Add capture (parent -> child) detection and apply default cursor
        dialogPane.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            Node target = (Node) e.getTarget();

            if (isInteractiveControl(target)) {
                dialogPane.setCursor(null);
            } 
        });
        
        Platform.runLater(() -> {
            // Now the node is attached to scene graph and can receive focus
            root.requestFocus();
        });
    }
    
    protected BorderPane getBorderPane(){
        return dialogPane;
    }
    
    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight){
        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);         
    }
    
    private void handleMouseMoved(MouseEvent e) {    
        
        if(!e.isPrimaryButtonDown()){
            Point2D p = dialogPane.sceneToLocal(e.getSceneX(), e.getSceneY());             
            currentResize = getResizeMode(p.getX(), p.getY(), dialogPane.getWidth(), dialogPane.getHeight());   
            dialogPane.setCursor(cursorForResizeMode(currentResize));
        }
    }
    
    private void handleMousePressed(MouseEvent e) {        
        if (currentResize != ResizeMode.NONE) {
            e.consume();  
            
            Point2D p = dialogPane.sceneToLocal(e.getSceneX(), e.getSceneY());     
           
            pressCenterX = root.getWidth()/2;//dialogPane.getLayoutX() + pressWidth / 2;
            pressCenterY = root.getHeight()/2;//dialogPane.getLayoutY() + pressHeight / 2;
                       
            edgeOffsetX = switch (currentResize) {
                case EAST, NORTH_EAST, SOUTH_EAST -> dialogPane.getWidth() - p.getX();
                case WEST, NORTH_WEST, SOUTH_WEST -> p.getX();
                default -> 0;
            };
            
            edgeOffsetY = switch (currentResize) {
                case SOUTH, SOUTH_EAST, SOUTH_WEST -> dialogPane.getHeight() - p.getY();
                case NORTH, NORTH_EAST, NORTH_WEST -> p.getY();
                default -> 0;
            };            
        }
       
    }
    
    private void handleMouseDragged(MouseEvent e) {  
        if (currentResize == ResizeMode.NONE) return;

        double px = e.getX();
        double py = e.getY();
        
        boolean widthChanged = false;
        boolean heightChanged = false;
        
        switch(currentResize){
            case NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST ->{
                widthChanged = true;
                heightChanged = true;
            } 
            case EAST, WEST -> widthChanged = true;
            case NORTH, SOUTH -> heightChanged = true;
        }
        
        if(widthChanged)
            dialogPane.setMaxWidth(Math.abs(pressCenterX - px) * 2 + edgeOffsetX);
        if(heightChanged)
            dialogPane.setMaxHeight(Math.abs(pressCenterY - py) * 2 + edgeOffsetY);        

        dialogPane.requestLayout();
    }
    
    private ResizeMode getResizeMode(double x, double y, double w, double h) {
        boolean left = x >= 0 && x <= RESIZE_MARGIN;
        boolean right = x >= w - RESIZE_MARGIN && x <= w;
        boolean top = y >= 0 && y <= RESIZE_MARGIN;
        boolean bottom = y >= h - RESIZE_MARGIN && y <= h;

        if (top && left) return ResizeMode.NORTH_WEST;
        if (top && right) return ResizeMode.NORTH_EAST;
        if (bottom && left) return ResizeMode.SOUTH_WEST;
        if (bottom && right) return ResizeMode.SOUTH_EAST;
        if (top) return ResizeMode.NORTH;
        if (bottom) return ResizeMode.SOUTH;
        if (left) return ResizeMode.WEST;
        if (right) return ResizeMode.EAST;
        return ResizeMode.NONE;
    }
    
    private Cursor cursorForResizeMode(ResizeMode mode) {
        return switch(mode){
            case EAST -> Cursor.E_RESIZE;
            case WEST -> Cursor.W_RESIZE;
            case NORTH -> Cursor.N_RESIZE;
            case SOUTH -> Cursor.S_RESIZE;
            case NORTH_EAST -> Cursor.NE_RESIZE;
            case NORTH_WEST -> Cursor.NW_RESIZE;
            case SOUTH_EAST -> Cursor.SE_RESIZE;
            case SOUTH_WEST -> Cursor.SW_RESIZE;
            default -> Cursor.DEFAULT;
        };
    }
            
    public void setHeaderTitle(String title) {
        header.setHeader(title);
    }

    public void setHeaderDescription(String description) {        
        header.setDescription(description);
    }
    
    public void setHeaderGraphic(Node graphic) {        
        header.setLeft(graphic);
    }

    public void setHeader(Node graphic, String title, String description) {
        header.setHeader(title);
        header.setDescription(description);
        header.setLeft(graphic);
    }
    
    public void setGraphic(Node graphic){
        header.setLeft(graphic);
    }

    public void setCloseButtonActive(boolean active) {
        switch(active){
            case true -> header.setRight(closeBtn);
            case false -> header.setRight(null);
        }
    }
    
    private boolean isInteractiveControl(Node node) {
        while (node != null) {
            if (node instanceof javafx.scene.control.Control control && control.isFocusTraversable()) {
                return true;
            }
            node = node.getParent(); // walk up in case wrapped in layout
        }
        return false;
    }
    
    protected void setDialogSize(double width, double height){
        dialogPane.setMaxSize(width, height);
        dialogPane.setPrefSize(width, height);
    }
    
    protected void setDialogWidth(double width){
        dialogPane.setMaxWidth(width);
        dialogPane.setPrefWidth(width);
    }
    
    protected void setDialogHeight(double height){
        dialogPane.setMaxHeight(height);
        dialogPane.setPrefHeight(height);
    }
}
