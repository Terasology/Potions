/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.potions;

import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.widgets.UILabel;

public class PotionStatusScreen extends CoreScreenLayer {

    private UILabel[] statusLabels = new UILabel[10];

    @Override
    public void initialise() {
        for (int i = 0; i < 10; i++) {
            statusLabels[i] = find("statusLabel" + (i + 1), UILabel.class);
        }
        removeAll();
    }

    public void addEffect(int index, String name, long duration) {
        statusLabels[index].setText(toTitleCase(name) + " " + (duration / 1000) + "s");
    }

    public void removeAll() {
        for (int i = 0; i < 10; i++) {
            statusLabels[i].setText("");
        }
    }

    //Tweaked from http://stackoverflow.com/a/1086134
    @Deprecated
    public static String toTitleCase(String input) {
        input = input.replace('_', ' ').toLowerCase();
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
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
