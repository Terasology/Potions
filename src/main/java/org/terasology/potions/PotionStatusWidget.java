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

import org.terasology.math.geom.Vector2i;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.CoreWidget;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.databinding.Binding;
import org.terasology.rendering.nui.databinding.ReadOnlyBinding;
import org.terasology.rendering.nui.layouts.ColumnLayout;
import org.terasology.rendering.nui.widgets.UILabel;

public class PotionStatusWidget extends CoreWidget {

    private ColumnLayout effects = new ColumnLayout();

    public PotionStatusWidget() {
        effects.setFillVerticalSpace(false);
        effects.addWidget(new UILabel("Potion Effects:"));
    }

    /**
     * Converts an effect name to Title Case
     *
     * @param string The effect name to convert
     * @return The effect in Title Case
     */
    private static String toTitleCase(String string) {
        String input = string.replace('_', ' ').toLowerCase();
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            titleCase.append(nextTitleCase ? Character.toTitleCase(c) : c);
            nextTitleCase = Character.isSpaceChar(c);
        }

        return titleCase.toString();
    }

    /**
     * Adds or updates an effect on the display
     *
     * @param effectId The effect ID
     * @param duration A binding giving the time remaining
     */
    public void addOrUpdateEffect(String effectId, Binding<Long> duration) {
        String effectName = toTitleCase(effectId);
        Binding<String> nameBinding = new ReadOnlyBinding<String>() {
            @Override
            public String get() {
                return String.format("%s %.1f", effectName, (float) duration.get() / 1000) + "s";
            }
        };

        UILabel target = findWidget(effectId);
        if (target == null) {
            effects.addWidget(new UILabel(effectId, nameBinding));
        } else {
            target.bindText(nameBinding);
        }
    }

    /**
     * Removes an effect from the list
     *
     * @param effectId The id of the effect to remove
     */
    public void removeEffect(String effectId) {
        for (UIWidget widget : effects) {
            if (widget.getId().equals(effectId)) {
                effects.removeWidget(widget);
                return;
            }
        }
    }

    /**
     * Locates a widget by the given id in the column
     *
     * @param effectId The id of the effect locate
     * @return The widget if one was found, null otherwise
     */
    private UILabel findWidget(String effectId) {
        for (UIWidget widget : effects) {
            if (widget.getId().equals(effectId)) {
                return (UILabel) widget;
            }
        }
        return null;
    }

    @Override
    public void onDraw(Canvas canvas) {
        effects.onDraw(canvas);
    }

    @Override
    public Vector2i getPreferredContentSize(Canvas canvas, Vector2i sizeHint) {
        return effects.getPreferredContentSize(canvas, sizeHint);
    }
}
