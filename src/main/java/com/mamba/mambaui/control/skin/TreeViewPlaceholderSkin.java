/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.control.skin;

import module javafx.controls;

/**
 *
 * @author jmburu
 */
public class TreeViewPlaceholderSkin<T> extends TreeViewSkin<T> {
    private StackPane placeholderRegion;
    private Label placeholderLabel;

    public TreeViewPlaceholderSkin(TreeView<T> control) {
        super(control);
        installPlaceholderSupport();
    }

    private void installPlaceholderSupport() {
        registerChangeListener(getSkinnable().rootProperty(), e -> updatePlaceholderSupport());
        updatePlaceholderSupport();
    }

    /**
     * Updating placeholder/flow visibilty depending on whether or not the tree
     * is considered empty. 
     * 
     * Basically copied from TableViewSkinBase.
     */
    private void updatePlaceholderSupport() {
        if (isTreeEmpty()) {
            if (placeholderRegion == null) {
                placeholderRegion = new StackPane();
                placeholderRegion.getStyleClass().setAll("placeholder");
                getChildren().add(placeholderRegion);

                placeholderLabel = new Label("No treeItems");
                placeholderRegion.getChildren().setAll(placeholderLabel);
            }
        }
        getVirtualFlow().setVisible(!isTreeEmpty());
        if (placeholderRegion != null)
            placeholderRegion.setVisible(isTreeEmpty());
    }


    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        if (placeholderRegion != null && placeholderRegion.isVisible()) {
            placeholderRegion.resizeRelocate(x, y, w, h);
        }
    }

    private boolean isTreeEmpty() {
        return getSkinnable().getRoot() == null;
    }
}
