// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.potions;

import org.terasology.engine.rendering.nui.CoreScreenLayer;
import org.terasology.nui.widgets.UILabel;

public class PotionStatusScreen extends CoreScreenLayer {

    private UILabel[] statusLabels = new UILabel[15];

    @Override
    public void initialise() {
        for (int i = 0; i < 15; i++) {
            statusLabels[i] = find("statusLabel" + (i + 1), UILabel.class);
        }
        removeAll();
    }

    public void addEffect(int index, String name, long duration) {
        statusLabels[index].setText(toTitleCase(name) + " " + (duration / 1000) + "s");
    }

    public void addEffect(int index, String name, float magnitude, long duration) {
        statusLabels[index].setText(toTitleCase(name) + " (" + magnitude + "u) " + (duration / 1000) + "s");
    }

    public void removeAll() {
        for (int i = 0; i < 15; i++) {
            statusLabels[i].setText("");
        }
    }

    //Tweaked from http://stackoverflow.com/a/1086134
    @Deprecated
    public static String toTitleCase(String input) {
        String inputString = input.replace('_', ' ').toLowerCase();
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : inputString.toCharArray()) {
            char character = c;
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                character = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    @Override
    public boolean isModal() {
        return false;
    }
}
