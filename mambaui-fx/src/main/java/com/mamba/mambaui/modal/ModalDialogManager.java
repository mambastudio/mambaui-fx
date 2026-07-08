package com.mamba.mambaui.modal;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public final class ModalDialogManager {

    private static final Set<ModalDialog<?>> activeDialogs = new CopyOnWriteArraySet<>();

    public static void register(ModalDialog<?> dialog) {
        activeDialogs.add(dialog);
    }

    public static void unregister(ModalDialog<?> dialog) {
        activeDialogs.remove(dialog);
    }

    public static void exitAll() {
        for (ModalDialog<?> dialog : activeDialogs) {
            dialog.close(); // This handles visibility and exiting loop safely
        }
        activeDialogs.clear();
    }
}
