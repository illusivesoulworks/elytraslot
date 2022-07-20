/*
 * Copyright (C) 2019-2022 Illusive Soulworks
 *
 * Elytra Slot is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Elytra Slot is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Elytra Slot. If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.elytraslot;

import com.illusivesoulworks.elytraslot.client.ElytraSlotLayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;

public class ElytraSlotFabricClientMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register(
        player -> !ElytraSlotCommonMod.isEquipped(player));
    LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
        (entityType, entityRenderer, registrationHelper, context) -> registrationHelper.register(
            new ElytraSlotLayer<>(entityRenderer, context.getModelSet())));
  }
}
