/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mamba.mambaui.control;


import com.mamba.mambaui.control.skin.TreeViewPlaceholderSkin;

import module javafx.controls;

/**
 *
 * @author jmburu
 * @param <T>
 */
public class PlaceholderTreeView<T> extends TreeView<T> {
    
    private final ObjectProperty<Node> placeholder =
            new SimpleObjectProperty<>(this, "placeholder");

    public final ObjectProperty<Node> placeholderProperty() {
        return placeholder;
    }

    public final Node getPlaceholder() {
        return placeholder.get();
    }

    public final void setPlaceholder(Node node) {
        placeholder.set(node);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TreeViewPlaceholderSkin<>(this);
    }
}
