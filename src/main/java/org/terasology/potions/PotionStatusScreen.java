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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.potions.component.PotionEffect;
import org.terasology.rendering.assets.texture.TextureRegion;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.widgets.UIImage;
import org.terasology.rendering.nui.widgets.UILabel;
import org.terasology.utilities.Assets;

import java.util.Arrays;

public class PotionStatusScreen extends CoreScreenLayer {
    private static final Logger logger = LoggerFactory.getLogger(PotionStatusScreen.class);

    private UIImage[] statusIcons = new UIImage[10];
    private UILabel[] statusNames = new UILabel[10];
    private UILabel[] statusDurations = new UILabel[10];

    @Override
    public void initialise() {
        for (int i = 0; i < 10; i++) {
            statusIcons[i] = find("statusIcon" + (i + 1), UIImage.class);
            statusNames[i] = find("statusName" + (i + 1), UILabel.class);
            statusDurations[i] = find("statusDuration" + (i + 1), UILabel.class);
        }
        logger.info(find("statusIcon1", UIImage.class).toString());
        for (int i = 0; i < 10; i++) {
            removeEffect(i);
        }
    }

    public void addEffect(int index, String name, long duration) {
//        TextureRegion icon =  Assets.getTextureRegion("potions:effectIcons#" + name).get();
  //      icon = icon != null ? icon : Assets.getTextureRegion("engine:icons#emptyIcon").get();
    //    statusIcons[index].setImage(icon);
        statusNames[index].setText(name);
        statusDurations[index].setText(String.valueOf(duration));
    }

    public void removeEffect(int index) {
        statusIcons[index].setImage(Assets.getTextureRegion("engine:icons#emptyIcon").get());
        statusNames[index].setText("");
        statusDurations[index].setText("");
    }

    @Override
    public boolean isModal() {
        return false;
    }
}
