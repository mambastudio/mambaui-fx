/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.control;

import com.mamba.mambaui.MambauiTheme;
import com.mamba.mambaui.base.RectLayout.Rect;
import com.mamba.mambaui.base.RectLayout.RectCut;
import com.mamba.mambaui.base.RectLayout.RectCutSide;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author user
 */
public class Tile extends Region{
    Node left, right;
    Label header, description;
    double margin = 5;
        
    record RectTile(Rect leftRect, Rect headerRect, Rect descriptionRect, Rect rightRect){
        RectTile{
            Objects.requireNonNull(leftRect);
            Objects.requireNonNull(headerRect);
            Objects.requireNonNull(descriptionRect);
            Objects.requireNonNull(rightRect);
        }

        public boolean hasLeft(){
            return !leftRect().hasNegligibleSize();
        }
        
        public boolean hasHeader(){
            return !headerRect().hasNegligibleSize();
        }
        
        public boolean hasDescription(){
            return !descriptionRect().hasNegligibleSize();
        }
        
        public boolean hasRight(){
            return !rightRect().hasNegligibleSize();
        }
        
        public Rect bound(){
            Rect bound = new Rect();
            return bound.include(leftRect).include(headerRect).include(descriptionRect).include(rightRect);            
        }
    }
    
    public Tile(){}
    public Tile(String header, String description){
        setHeader(header);
        setDescription(description);
    }
    
    public Tile(String header, String description, Node right){
        this(header, description);
        setRight(right);
    }
    
    public Tile(Node left, String header, String description, Node right){
        this(header, description, right);
        setLeft(left);
    }
    
    public final void setLeft(Node node) {
        updateSlot(this.left, node);
        this.left = node;
    }

    public final void setRight(Node node) {
        updateSlot(this.right, node);
        this.right = node;
    }

    public final void setHeader(String string) {
        Label label = new Label(string);
        updateSlot(this.header, label);
        this.header = label;
    }

    public final void setDescription(String string) {
        Label label = new Label(string);
        label.setWrapText(true);
        updateSlot(this.description, label);
        this.description = label;
        this.description.setAlignment(Pos.TOP_LEFT);
    }
    
    private void updateSlot(Node oldNode, Node newNode) {
        if (oldNode != null) {
            super.getChildren().remove(oldNode);
        }
        if (newNode != null) {
            super.getChildren().add(newNode);
        }
    }
    
    @Override
    public ObservableList<Node> getChildren(){
        return FXCollections.unmodifiableObservableList(super.getChildren());
    }
    
    private Rect viewBound(){
        double x = getInsets().getLeft();
        double y = getInsets().getTop();
        double w = getWidth() - x - getInsets().getRight();
        double h = getHeight() - y - getInsets().getBottom(); 
        
        return new Rect(x, y, w, h);
    }
    
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        
        double x = getInsets().getLeft();
        double y = getInsets().getTop();
        double w = getWidth() - x - getInsets().getRight();
        double h = getHeight() - y - getInsets().getBottom(); 
                
        Rectangle clip = (Rectangle) getClip();
        if (clip == null) {
            clip = new Rectangle();
            setClip(clip);
        }
        
        clip.setX(x);
        clip.setY(y);
        clip.setWidth(w);
        clip.setHeight(h);
        
        if (w < 10 || h < 10) return;
        
        RectCut layoutRect = new RectCut(x, y, x + w, y + h);    
        
        //IO.println(this.getParent().prefWidth(-1));
        RectTile tileLayout = getRects(layoutRect);
        
        
                               
        if(tileLayout.hasLeft()){            
            layoutInArea(
                    left, 
                    tileLayout.leftRect.minx(), 
                    tileLayout.leftRect.miny(), 
                    tileLayout.leftRect.width(), 
                    tileLayout.leftRect.height(), 
                    0, 
                    HPos.LEFT, VPos.TOP);
        }
        
        if(tileLayout.hasHeader()){
            layoutInArea(
                    header, 
                    tileLayout.headerRect.minx(), 
                    tileLayout.headerRect.miny(), 
                    tileLayout.headerRect.width(), 
                    tileLayout.headerRect.height(), 
                    0,
                    HPos.LEFT, VPos.CENTER);
        }
        
        if(tileLayout.hasDescription()){
            layoutInArea(
                    description, 
                    tileLayout.descriptionRect.minx(), 
                    tileLayout.descriptionRect.miny(), 
                    tileLayout.descriptionRect.width(), 
                    tileLayout.descriptionRect.height(), 
                    0, 
                    HPos.LEFT, VPos.TOP);
        }
        
        if(tileLayout.hasRight()){
            layoutInArea(
                    right, 
                    tileLayout.rightRect.minx(), 
                    tileLayout.rightRect.miny(), 
                    tileLayout.rightRect.width(), 
                    tileLayout.rightRect.height(), 
                    0, 
                    HPos.RIGHT, VPos.TOP);
        } 
        
    }
    
    @Override
    protected double computeMinWidth(double height) {   
        Rect minRect = minRect();
        return minRect.width();
    }

    @Override
    protected double computeMinHeight(double width) {
        Rect minRect = minRect();
        return minRect.height();
    }
    
    @Override
    protected double computePrefWidth(double height){   
        return Double.MAX_VALUE;
    }
        
    @Override
    protected double computePrefHeight(double width){    
        if (width <= 0 || Double.isNaN(width)) 
            return 0;            
        return getRects(new RectCut(viewBound())).bound().height() + totalInsetsHeight();
    }
    
    
    @Override
    protected double computeMaxWidth(double height){               
        return Double.MAX_VALUE;
    }
    
            
    private RectTile getRects(RectCut rectCut){
        Rect lRect = new Rect(), hRect = new Rect(), dRect = new Rect(), rRect = new Rect();
        
        
        
        if(left != null){
            rectCut = rectCut.cutRemaining(RectCutSide.LEFT, left.prefWidth(-1));
            lRect = rectCut.rect().cutTop(left.prefHeight(-1)).first(); //exact node dimensions now
            rectCut = rectCut.expand(margin);            
        }
        
        if(right != null){
            rectCut = rectCut.cutRemaining(RectCutSide.RIGHT, right.prefWidth(-1));            
            rRect = rectCut.rect().cutTop(right.prefHeight(-1)).first(); //exact node dimensions now
            rectCut = rectCut.expand(margin);
        }
        
        if(header != null){
            
            rectCut = rectCut.cutRemaining(RectCutSide.TOP, header.prefHeight(rectCut.remainingRect().width()));            
            hRect = rectCut.rect();
            rectCut = rectCut.expand(margin);            
        }
        
        if(description != null){              
            double dHeight = description.prefHeight(rectCut.width());
            dRect = new Rect(0, 0, rectCut.width(), dHeight);
            dRect = rectCut.placeBottom(dRect, 0);
            
            double limit = prefHeight(-1) - totalInsetsHeight();            
            if(limit > 0 && dRect.maxy() > limit)
                dRect = new Rect(dRect.minx(), dRect.miny(), dRect.maxx(), limit);
        }
        
        return new RectTile(lRect, hRect, dRect, rRect);
    }  
        
    private Rect minRect(){
        Rect lRect = new Rect(), hRect = new Rect(), rRect = new Rect();
        
        if(left != null)            
            lRect = new Rect(left.prefWidth(-1), left.prefHeight(-1));
        
        if(header != null){
            hRect = new Rect(header.prefWidth(-1), header.prefHeight(-1));    
            
            if(hRect.width() < 10)
                hRect = new Rect(10, hRect.height());
            if(hRect.height()< 10)
                hRect = new Rect(hRect.width(), 10);
            
            hRect = lRect.placeRight(hRect, margin);            
        }
                
        if(right != null){
            rRect = new Rect(right.prefWidth(-1), right.prefHeight(-1));
            rRect = hRect.placeRight(rRect, margin);
        }
                
        return lRect.include(hRect).include(rRect);
    }
    
    private double totalInsetsHeight(){
        return getInsets().getTop() + getInsets().getBottom();
    }
    
    @Override
    public String getUserAgentStylesheet() {
        return MambauiTheme.SHEET; // shared master file with @import "tile.css"
    }
}
