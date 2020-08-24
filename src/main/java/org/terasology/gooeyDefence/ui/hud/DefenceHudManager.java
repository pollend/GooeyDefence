/*
 * Copyright 2018 MovingBlocks
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
package org.terasology.gooeyDefence.ui.hud;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.gooeyDefence.DefenceUris;
import org.terasology.gooeyDefence.StatSystem;
import org.terasology.gooeyDefence.waves.OnWaveEnd;
import org.terasology.math.geom.Rect2f;
import org.terasology.nui.databinding.ReadOnlyBinding;
import org.terasology.nui.widgets.UIIconBar;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;
import org.terasology.rendering.nui.layers.hud.HealthHud;

/**
 * Manages displaying and setting all the hud elements.
 *
 * @see DefenceHud
 */
@RegisterSystem
public class DefenceHudManager extends BaseComponentSystem {
    @In
    private NUIManager nuiManager;

    @In
    private StatSystem statSystem;

    private DefenceHud defenceHud;

    @Override
    public void postBegin() {
        defenceHud = nuiManager.getHUD().addHUDElement(DefenceUris.DEFENCE_HUD, DefenceHud.class, Rect2f.createFromMinAndSize(0, 0, 1, 1));
        HealthHud healthHud = nuiManager.getHUD().getHUDElement(DefenceUris.HEALTH_HUD, HealthHud.class);
        UIIconBar healthBar = healthHud.find("healthBar", UIIconBar.class);

        defenceHud.updateCurrentWave();

        healthBar.bindMaxValue(new ReadOnlyBinding<Float>() {
            @Override
            public Float get() {
                return (float) statSystem.getMaxHealth();
            }
        });
        healthBar.bindValue(new ReadOnlyBinding<Float>() {
            @Override
            public Float get() {
                return (float) statSystem.getShrineHealth();
            }
        });
    }

    /**
     * Updates the wave displayed in the HUD
     * <p>
     * Called when a wave is ended
     *
     * @see OnWaveEnd
     */
    @ReceiveEvent
    public void onWaveEnd(OnWaveEnd event, EntityRef entity) {
        defenceHud.updateCurrentWave();
    }
}
