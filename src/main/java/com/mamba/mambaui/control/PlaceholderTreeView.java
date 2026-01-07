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
 */
public class PlaceholderTreeView<T> extends TreeView<T> {

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TreeViewPlaceholderSkin<>(this);
    }
}
