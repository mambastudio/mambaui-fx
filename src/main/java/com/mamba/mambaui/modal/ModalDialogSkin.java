/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.modal;

import com.mamba.mambaui.MambauiUtility;
import com.mamba.mambaui.control.Tile;
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
    
    private static final int RESIZE_MARGIN = 8;
    private ResizeMode currentResize = ResizeMode.NONE;
        
    private double pressWidth = 0;
    private double pressHeight = 0;
    private double pressCenterX = 0;
    private double pressCenterY = 0;
    
    double edgeOffsetX, edgeOffsetY;
    
    protected final StackPane root;
    protected final BorderPane dialogPane = new BorderPane();
    protected Tile header = new Tile();
    protected Button closeBtn = MambauiUtility.buttonIcon("mdal-close", 30, "circular-button", "close-icon");
    
    public ModalDialogSkin(ModalDialog dialog) {
        super(dialog); 
        
        setHeader(dialog.getHeaderGraphic(), dialog.getHeaderTitle(), dialog.getHeaderDescription());
        setCloseButtonActive(dialog.getHeaderCloseButtonActive());

        dialog.headerTitleProperty().addListener((obs, oldVal, newVal) -> {
            setHeaderTitle(newVal);
        });
        dialog.headerDescriptionProperty().addListener((obs, oldVal, newVal) -> {
            setHeaderDescription(newVal);
        });
        dialog.headerGraphicProperty().addListener((obs, oldVal, newVal) -> {
            setHeaderGraphic((Node) newVal);
        });        
        dialog.headerCloseButtonActiveProperty().addListener((obs, oldVal, newVal) ->{
            setCloseButtonActive(newVal);
        });
            
        root = new StackPane();
        initGraphics();        
    }
    
    private void initGraphics(){
        this.dialogPane.getStyleClass().add("pane"); 
        header.setPrefHeight(90);
        dialogPane.setTop(header);
        
        root.getChildren().add(dialogPane); //add pane first to root stack
        getChildren().add(root); //add root to skin
        
        dialogPane.setMaxSize(300, 300);
        dialogPane.setPrefSize(300, 300);
        
        dialogPane.setOnMouseMoved(this::handleMouseMoved);
        dialogPane.setOnMousePressed(this::handleMousePressed);
        dialogPane.setOnMouseDragged(this::handleMouseDragged);
        
        //Re-center automatically when parent resizes
        dialogPane.parentProperty().addListener((obs, oldParent, newParent) -> {
            if (newParent instanceof Pane parent) {
                parent.widthProperty().addListener((o, ov, nv) -> dialogPane.requestLayout());
                parent.heightProperty().addListener((o, ov, nv) -> dialogPane.requestLayout());
            }
        });
        
        closeBtn.setOnMouseEntered(e-> closeBtn.setCursor(Cursor.DEFAULT));
       
        header.setPadding(new Insets(5));
    }
    
    protected BorderPane getBorderPane(){
        return dialogPane;
    }
    
    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight){
        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);         
    }
    
    private void handleMouseMoved(MouseEvent e) {
        dialogPane.setCursor(cursorForResizeMode(currentResize));
        currentResize = getResizeMode(e.getX(), e.getY(), dialogPane.getWidth(), dialogPane.getHeight());
        
    }
    
    private void handleMousePressed(MouseEvent e) {
        if (currentResize != ResizeMode.NONE) {
            e.consume();
            pressWidth = dialogPane.getWidth();
            pressHeight = dialogPane.getHeight();

            pressCenterX = dialogPane.getLayoutX() + pressWidth / 2;
            pressCenterY = dialogPane.getLayoutY() + pressHeight / 2;
            
            edgeOffsetX = switch (currentResize) {
                case EAST, NORTH_EAST, SOUTH_EAST -> dialogPane.getWidth() - e.getX();
                case WEST, NORTH_WEST, SOUTH_WEST -> e.getX();
                default -> 0;
            };
            
            edgeOffsetY = switch (currentResize) {
                case SOUTH, SOUTH_EAST, SOUTH_WEST -> dialogPane.getHeight() - e.getY();
                case NORTH, NORTH_EAST, NORTH_WEST -> e.getY();
                default -> 0;
            };
        }
       
    }
    
    private void handleMouseDragged(MouseEvent e) {   
        if (currentResize == ResizeMode.NONE) return;

        Point2D p = point(e);
        double px = p.getX();
        double py = p.getY();
        
        double dx = px - pressCenterX;
        double dy = py - pressCenterY;

        double halfMinW = dialogPane.getMinWidth() / 2;
        double halfMinH = dialogPane.getMinHeight() / 2;

        double newWidth = dialogPane.getWidth();
        double newHeight = dialogPane.getHeight();

        boolean widthChanged = false;
        boolean heightChanged = false;

        switch (currentResize) {
            case EAST, NORTH_EAST, SOUTH_EAST -> {
                if (dx > halfMinW) {
                    newWidth = 2 * dx + edgeOffsetX;
                    widthChanged = true;
                }
            }
            case WEST, NORTH_WEST, SOUTH_WEST -> {
                if (dx < -halfMinW) {
                    newWidth = -2 * dx + edgeOffsetX;
                    widthChanged = true;
                }
            }
        }

        switch (currentResize) {
            case SOUTH, SOUTH_EAST, SOUTH_WEST -> {
                if (dy > halfMinH) {
                    newHeight = 2 * dy + edgeOffsetY;
                    heightChanged = true;
                }
            }
            case NORTH, NORTH_EAST, NORTH_WEST -> {
                if (dy < -halfMinH) {
                    newHeight = -2 * dy + edgeOffsetY;
                    heightChanged = true;
                }
            }
        }

        if (widthChanged) {
            dialogPane.setMaxWidth(newWidth);
            pressWidth = newWidth; // update reference point            
        }

        if (heightChanged) {
            dialogPane.setMaxHeight(newHeight);
            pressHeight = newHeight; // update reference point
        }

        dialogPane.requestLayout();
    }
    
    private ResizeMode getResizeMode(double x, double y, double w, double h) {
        boolean left = x < RESIZE_MARGIN;
        boolean right = x > w - RESIZE_MARGIN;
        boolean top = y < RESIZE_MARGIN;
        boolean bottom = y > h - RESIZE_MARGIN;

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
    
    private Point2D point(MouseEvent e){
        double px = e.getSceneX() - dialogPane.getParent().localToScene(0, 0).getX();
        double py = e.getSceneY() - dialogPane.getParent().localToScene(0, 0).getY();
        return new Point2D(px, py);
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
}
