/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.modal;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author user
 */
public class ModalDialogSkin extends SkinBase<ModalDialog> implements Skin<ModalDialog> {    
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
    protected final ModalDialog dialog; 
    protected final BorderPane pane = new BorderPane();
    
    public ModalDialogSkin(ModalDialog dialog) {
        super(dialog); 
        this.dialog = dialog;
        
        root = new StackPane();
    }
    
    protected BorderPane getBorderPane(){
        return pane;
    }
    
    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight){
        super.layoutChildren(contentX, contentY, contentWidth, contentHeight); 
        
        pane.resizeRelocate(contentX, contentY, contentWidth, contentHeight);
        pane.setMaxSize(contentWidth, contentHeight);
    }
    
    private void handleMouseMoved(MouseEvent e) {
        dialog.setCursor(cursorForResizeMode(currentResize));
        currentResize = getResizeMode(e.getX(), e.getY(), dialog.getWidth(), dialog.getHeight());
        
    }
    
    private void handleMousePressed(MouseEvent e) {
        if (currentResize != ResizeMode.NONE) {
            e.consume();
            pressWidth = dialog.getWidth();
            pressHeight = dialog.getHeight();

            pressCenterX = dialog.getLayoutX() + pressWidth / 2;
            pressCenterY = dialog.getLayoutY() + pressHeight / 2;
            
            edgeOffsetX = switch (currentResize) {
                case EAST, NORTH_EAST, SOUTH_EAST -> dialog.getWidth() - e.getX();
                case WEST, NORTH_WEST, SOUTH_WEST -> e.getX();
                default -> 0;
            };
            
            edgeOffsetY = switch (currentResize) {
                case SOUTH, SOUTH_EAST, SOUTH_WEST -> dialog.getHeight() - e.getY();
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

        double halfMinW = dialog.getMinWidth() / 2;
        double halfMinH = dialog.getMinHeight() / 2;

        double newWidth = dialog.getWidth();
        double newHeight = dialog.getHeight();

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
            dialog.setMaxWidth(newWidth);
            pressWidth = newWidth; // update reference point
        }

        if (heightChanged) {
            dialog.setMaxHeight(newHeight);
            pressHeight = newHeight; // update reference point
        }

        dialog.requestLayout();
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
        double px = e.getSceneX() - dialog.getParent().localToScene(0, 0).getX();
        double py = e.getSceneY() - dialog.getParent().localToScene(0, 0).getY();
        return new Point2D(px, py);
    }   
}
