/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.control.skin;

import com.mamba.mambaui.control.PlaceholderTreeView;

import module javafx.controls;

/**
 *
 * @author jmburu
 * @param <T>
 */
public class TreeViewPlaceholderSkin<T> extends TreeViewSkin<T> {

    private StackPane placeholderRegion;

    public TreeViewPlaceholderSkin(TreeView<T> control) {
        super(control);
        installPlaceholderSupport();
    }

    private void installPlaceholderSupport() {
        // listen for root changes
        registerChangeListener(getSkinnable().rootProperty(),
                e -> updatePlaceholderSupport());

        // listen for placeholder changes
        if (getSkinnable() instanceof PlaceholderTreeView<?> ptv) {
            registerChangeListener(
                ptv.placeholderProperty(),
                e -> updatePlaceholderContent()
            );
        }

        updatePlaceholderSupport();
        updatePlaceholderContent();
    }

    private void updatePlaceholderSupport() {
        if (isTreeEmpty()) {
            if (placeholderRegion == null) {
                placeholderRegion = new StackPane();
                placeholderRegion.getStyleClass().setAll("placeholder");
                getChildren().add(placeholderRegion);
            }
        }

        getVirtualFlow().setVisible(!isTreeEmpty());

        if (placeholderRegion != null) {
            placeholderRegion.setVisible(isTreeEmpty());
        }
    }

    private void updatePlaceholderContent() {
        if (!(getSkinnable() instanceof PlaceholderTreeView<?> ptv))
            return;

        if (placeholderRegion == null)
            return;

        var placeholder = ptv.getPlaceholder();

        if (placeholder != null) {
            placeholderRegion.getChildren().setAll(placeholder);
        } else {
            // default fallback
            placeholderRegion.getChildren().setAll(
                new Label("No tree items")
            );
        }
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
