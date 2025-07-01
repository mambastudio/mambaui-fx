/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mamba.mambaui.base;


import static com.mamba.mambaui.base.RectLayout.RectCutSide.BOTTOM;
import static com.mamba.mambaui.base.RectLayout.RectCutSide.LEFT;
import static com.mamba.mambaui.base.RectLayout.RectCutSide.RIGHT;
import static com.mamba.mambaui.base.RectLayout.RectCutSide.TOP;
import static java.lang.Double.max;
import static java.lang.Double.min;
import java.util.Objects;

/**
 *
 * @author user
 * 
 * Immutable form of RectCut from https://halt.software/p/rectcut-for-dead-simple-ui-layouts
 * 
 */
public sealed interface RectLayout {
    public enum RectCutSide {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
    };
      
    public double width();
    public double height();
    public double widthHalf();
    public double heightHalf();
    public boolean hasNegligibleSize();
        
    public record RectPair(Rect first, Rect second){}    
    public record Rect(double minx, double miny, double maxx, double maxy) implements RectLayout{
        
        public Rect{
            if (minx == Double.POSITIVE_INFINITY && miny == Double.POSITIVE_INFINITY && maxx == Double.NEGATIVE_INFINITY && maxy == Double.NEGATIVE_INFINITY) { //even if we translate, it still remains same
                // Do nothing, keep the infinities as is
            } 
            else if(width() < 0 || height() < 0)
                throw new UnsupportedOperationException("wrong dimension");
        }
        
        public Rect(){
            this(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY); //for bounding box (when you grow from minimum)
        }
        
        public Rect(double maxx, double maxy){
            this(0, 0, maxx, maxy);
        }
        
        public boolean isEmpty() {
            return this.minx == Double.POSITIVE_INFINITY
                && this.miny == Double.POSITIVE_INFINITY
                && this.maxx == Double.NEGATIVE_INFINITY
                && this.maxy == Double.NEGATIVE_INFINITY;
        }
        
        public boolean isNotEmpty(){
            return !isEmpty();
        }
        
        @Override
        public double width(){
            return maxx - minx;
        }
        
        @Override
        public double height(){
            return maxy - miny;
        }
        
        @Override
        public double widthHalf(){
            return width() * 0.5;
        }
        
        @Override
        public double heightHalf(){
            return height() * 0.5;
        }
        
        public boolean canCutWidth(double value) {
            return value > 0 && width() > value;
        }

        public boolean canCutHeight(double value) {
            return value > 0 && height() > value;
        }
        
        public boolean canCut(double widthValue, double heightValue) {
            return canCutWidth(widthValue) && canCutHeight(heightValue);
        }
        
        @Override
        public boolean hasNegligibleSize(){
            return width() < 5 || height() < 5;
        }
        
        // bounding box
        public Rect include(double x, double y) {
            if (Double.isNaN(x) || Double.isNaN(y)) return this;
            return new Rect(min(minx(), x),
                            min(miny(), y),            
                            max(maxx(), x),
                            max(maxy(), y));
        }
       
        // bounding box
        public Rect include(Rect rect){
            if (rect.isEmpty()) return this;
            if (this.isEmpty()) return rect;
            return new Rect(
                Math.min(this.minx, rect.minx),
                Math.min(this.miny, rect.miny),
                Math.max(this.maxx, rect.maxx),
                Math.max(this.maxy, rect.maxy)
            );
        }
               
        public RectPair cut(RectCutSide side, double amount) {
            Objects.requireNonNull(side);            
            return switch (side) {
                case LEFT -> cutLeft(amount);
                case RIGHT -> cutRight(amount);
                case TOP -> cutTop(amount);
                case BOTTOM -> cutBottom(amount);                
            };
        }
        
        public RectPair cutLeft(double amount) {
            if (this.isEmpty())
                throw new IllegalStateException("Cannot cut to an uninitialized Rect.");
            
            double cutmaxx = Math.min(this.maxx, this.minx + amount);
            Rect cut = new Rect(this.minx, this.miny, cutmaxx, this.maxy);
            Rect remaining = new Rect(cutmaxx, this.miny, this.maxx, this.maxy);
            return new RectPair(cut, remaining);
        }

        public RectPair cutRight(double amount) {
            if (this.isEmpty())
                throw new IllegalStateException("Cannot cut to an uninitialized Rect.");
            
            double cutminx = Math.max(this.minx, this.maxx - amount);
            Rect cut = new Rect(cutminx, this.miny, this.maxx, this.maxy);
            Rect remaining = new Rect(this.minx, this.miny, cutminx, this.maxy);
            return new RectPair(cut, remaining);
        }

        public RectPair cutTop(double amount) {
            if (this.isEmpty())
                throw new IllegalStateException("Cannot cut to an uninitialized Rect.");
            
            double cutmaxy = Math.min(this.maxy, this.miny + amount);            
            Rect cut = new Rect(this.minx, this.miny, this.maxx, cutmaxy);
            Rect remaining = new Rect(this.minx, cutmaxy, this.maxx, this.maxy);
            return new RectPair(cut, remaining);
        }

        public RectPair cutBottom(double amount) {
            if (this.isEmpty())
                throw new IllegalStateException("Cannot cut to an uninitialized Rect.");
            
            double cutminy = Math.max(this.miny, this.maxy - amount);
            Rect cut = new Rect(this.minx, cutminy, this.maxx, this.maxy);
            Rect remaining = new Rect(this.minx, this.miny, this.maxx, cutminy);
            return new RectPair(cut, remaining);
        }
        
        public Rect extend(RectCutSide side, double amount) {
            Objects.requireNonNull(side);            
            return switch (side) {
                case LEFT -> extendLeft(amount);
                case RIGHT -> extendRight(amount);
                case TOP -> extendTop(amount);
                case BOTTOM -> extendBottom(amount);                
            };
        }
        
        public Rect extendLeft(double amount){
            if (this.isEmpty())
                throw new IllegalStateException("Cannot extend to an uninitialized Rect.");
            
            double newCutMinX = minx - amount;
            return new Rect(newCutMinX, miny, maxx, maxy);
        }
        
        public Rect extendRight(double amount){
            if (this.isEmpty())
                throw new IllegalStateException("Cannot extend to an uninitialized Rect.");
            
            double newCutMaxX = maxx + amount;
            return new Rect(minx, miny, newCutMaxX, maxy);
        }
        
        public Rect extendBottom(double amount){
            if (this.isEmpty())
                throw new IllegalStateException("Cannot extend to an uninitialized Rect.");
            
            double newCutMaxY = maxy + amount;
            return new Rect(minx, miny, maxx, newCutMaxY);
        }
        
        public Rect extendTop(double amount){
            if (this.isEmpty())
                throw new IllegalStateException("Cannot extend to an uninitialized Rect.");
            
            double newCutMinY = miny - amount;
            return new Rect(minx, newCutMinY, maxx, maxy);
        }
        
        public Rect contract(RectCutSide side, double amount) {
            Objects.requireNonNull(side);            
            return switch (side) {
                case LEFT -> contractLeft(amount);
                case RIGHT -> contractRight(amount);
                case TOP -> contractTop(amount);
                case BOTTOM -> contractBottom(amount);                
            };
        }
        
        public Rect contractLeft(double amount){
            if (this.isEmpty())
                throw new IllegalStateException("Cannot contract to an uninitialized Rect.");
            
            double newCutMinX = minx + amount;
            if(maxx - newCutMinX < 0)
                newCutMinX = maxx;
            return new Rect(newCutMinX, miny, maxx, maxy);
        }
                
        public Rect contractRight(double amount){
            if (this.isEmpty())
                throw new IllegalStateException("Cannot contract to an uninitialized Rect.");
            
            double newCutMaxX = maxx - amount;
            if(newCutMaxX - minx < 0)
                newCutMaxX = minx;
            return new Rect(minx, miny, newCutMaxX, maxy);
        }
        
        public Rect contractBottom(double amount){
            if (this.isEmpty())
                throw new IllegalStateException("Cannot contract to an uninitialized Rect.");
            
            double newCutMaxY = maxy - amount;
            if(newCutMaxY - miny < 0)
                newCutMaxY = miny;
            return new Rect(minx, miny, maxx, newCutMaxY);
        }
        
        public Rect contractTop(double amount){
            if (this.isEmpty())
                throw new IllegalStateException("Cannot contract to an uninitialized Rect.");
            
            double newCutMinY = miny + amount;
            if(maxy - newCutMinY < 0)
                newCutMinY = maxy;
            return new Rect(minx, newCutMinY, maxx, maxy);
        }
        
        public Rect placeLeft(Rect rect, double amount) {
            if (this.isEmpty())
                throw new IllegalStateException("Cannot place relative to an uninitialized Rect.");
            
            double minX = this.minx - rect.width() - amount;            
            double minY = miny;
            double maxX = minX + rect.width();
            double maxY = minY + rect.height();
            return new Rect(minX, minY, maxX, maxY);
        }
        
        public Rect placeRight(Rect rect, double amount) {
            if (this.isEmpty())
                throw new IllegalStateException("Cannot place relative to an uninitialized Rect.");
            
            double width = rect.width();
            double minX = maxx + amount;
            double minY = miny;
            double maxX = minX + width;            
            double maxY = minY + rect.height();
            return new Rect(minX, minY, maxX, maxY);
        }

        public Rect placeTop(Rect rect, double amount) {
            if (this.isEmpty())
                throw new IllegalStateException("Cannot place relative to an uninitialized Rect.");
            
            double minX = minx;
            double minY = miny - rect.height() - amount;
            double maxX = minX + rect.width();
            double maxY = minY + rect.height();
            return new Rect(minX, minY, maxX, maxY);
        }
        
        public Rect placeBottom(Rect rect, double amount) {
            if (this.isEmpty())
                throw new IllegalStateException("Cannot place relative to an uninitialized Rect.");
            
            double minX = minx;
            double minY = maxy + amount;
            double maxX = minX + rect.width();
            double maxY = minY + rect.height();        
            return new Rect(minX, minY, maxX, maxY);
        }
    }
    
    //preserves volume and immutable like Rect
    public record RectCut(Rect rect, Rect remainingRect, RectCutSide side) implements RectLayout{
        public RectCut{
            Objects.requireNonNull(rect);
            Objects.requireNonNull(remainingRect);
            Objects.requireNonNull(side);
        }
        
        public RectCut(Rect rect, RectCutSide side){
            this(rect, rect, side);
        }
        
        public RectCut(Rect rect){
            this(rect, rect, RectCutSide.LEFT);
        }
        
        public RectCut(double minx, double miny, double maxx, double maxy){
            this(new Rect(minx, miny, maxx, maxy));
        }
        
        public boolean isRoot(){
            return remainingRect == rect;
        }
        
        public RectCut cutRemaining(RectCutSide side, double amount) {
            Objects.requireNonNull(side);            
            return switch (side) {
                case LEFT -> {
                    RectPair pair = remainingRect.cutLeft(amount);
                    yield new RectCut(pair.first, pair.second, LEFT);
                }
                case RIGHT -> {
                    RectPair pair = remainingRect.cutRight(amount);
                    yield new RectCut(pair.first, pair.second, RIGHT);
                }
                case TOP -> {
                    RectPair pair = remainingRect.cutTop(amount);
                    yield new RectCut(pair.first, pair.second, TOP);
                }
                case BOTTOM -> {
                    RectPair pair = remainingRect.cutBottom(amount);  
                    yield new RectCut(pair.first, pair.second, BOTTOM);
                }
            };
        }
        
        public RectCut cutRemaining(double amount) {
            return cutRemaining(side, amount);
        }       
        
        public RectCut expand(double amount) {
            if (amount <= 0 || Double.isNaN(amount) || isRoot()) return this;
            
            RectCut cut = switch (side) {
                case LEFT -> new RectCut(rect.extendRight(amount), remainingRect.contractLeft(amount),  LEFT); 
                case RIGHT -> new RectCut(rect.extendLeft(amount), remainingRect.contractRight(amount), RIGHT);
                case TOP -> new RectCut(rect.extendBottom(amount), remainingRect.contractTop(amount), TOP);
                case BOTTOM -> new RectCut(rect.extendTop(amount), remainingRect.contractBottom(amount), BOTTOM);
            };
            
            if(cut.remainingRect.hasNegligibleSize())
                cut = switch(side){
                    case LEFT -> new RectCut(rect.extendRight(cut.remainingRect.minx - rect.maxx), cut.remainingRect,  LEFT); 
                    case RIGHT -> new RectCut(rect.extendLeft(rect.minx - cut.remainingRect.maxx), cut.remainingRect, RIGHT);
                    case TOP -> new RectCut(rect.extendBottom(cut.remainingRect.miny - rect.maxy), cut.remainingRect, TOP);
                    case BOTTOM -> new RectCut(rect.extendTop(rect.miny - cut.remainingRect.maxy), cut.remainingRect, BOTTOM);
                };            
            return cut;                   
        }


        public RectCut contract(double amount) {
            if (amount <= 0 || Double.isNaN(amount)) return this;

            RectCut cut = switch (side) {
                case LEFT -> new RectCut(rect.contractRight(amount), remainingRect.extendLeft(amount),  LEFT);                
                case RIGHT -> new RectCut(rect.contractLeft(amount), remainingRect.extendRight(amount), RIGHT);
                case TOP -> new RectCut(rect.contractBottom(amount), remainingRect.extendTop(amount), TOP);
                case BOTTOM -> new RectCut(rect.contractTop(amount), remainingRect.extendBottom(amount), BOTTOM);
            };
            
            if(cut.rect.hasNegligibleSize())
                cut = switch (side) {
                    case LEFT -> new RectCut(cut.rect, remainingRect.extendLeft(remainingRect.minx - cut.rect.maxx),  LEFT);                
                    case RIGHT -> new RectCut(cut.rect, remainingRect.extendRight(cut.rect.minx - remainingRect.maxx), RIGHT);
                    case TOP -> new RectCut(cut.rect, remainingRect.extendTop(remainingRect.miny - cut.rect.maxy), TOP);
                    case BOTTOM -> new RectCut(cut.rect, remainingRect.extendBottom(cut.rect.miny - remainingRect.maxy), BOTTOM);
                };            
            return cut;
        }
        
        public Rect placeLeft(Rect rect, double amount) {return this.rect.placeLeft(rect, amount);}        
        public Rect placeRight(Rect rect, double amount) {return this.rect.placeRight(rect, amount);}
        public Rect placeTop(Rect rect, double amount) {return this.rect.placeTop(rect, amount);}        
        public Rect placeBottom(Rect rect, double amount) {return this.rect.placeBottom(rect, amount);}
        
        public Rect placeLeft(RectCut rectCut, double amount) {return this.rect.placeLeft(rectCut.rect(), amount);}        
        public Rect placeRight(RectCut rectCut, double amount) {return this.rect.placeRight(rectCut.rect(), amount);}
        public Rect placeTop(RectCut rectCut, double amount) {return this.rect.placeTop(rectCut.rect(), amount);}        
        public Rect placeBottom(RectCut rectCut, double amount) {return this.rect.placeBottom(rectCut.rect(), amount);}

        public RectCut remaining(){
            return new RectCut(remainingRect, side);
        }
        
        public boolean hasRemainingPortion(){
            return !remainingRect.hasNegligibleSize();
        }
        
        @Override
        public double width(){
            return rect.width();
        }
        
        @Override
        public double height(){
            return rect.height();
        }
        
        @Override
        public double widthHalf(){
            return rect.widthHalf();
        }
        
        @Override
        public double heightHalf(){
            return rect.heightHalf();
        }

        @Override
        public boolean hasNegligibleSize() {
            return rect.hasNegligibleSize();
        }
    }
}
