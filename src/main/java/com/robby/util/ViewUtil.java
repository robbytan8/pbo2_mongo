package com.robby.util;

import javafx.scene.control.TextInputControl;

/**
 *
 * @author Robby
 */
public final class ViewUtil {

    public static boolean isEmptyField(TextInputControl... textInputControls) {
        for (TextInputControl textInputControl : textInputControls) {
            if (!textInputControl.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
