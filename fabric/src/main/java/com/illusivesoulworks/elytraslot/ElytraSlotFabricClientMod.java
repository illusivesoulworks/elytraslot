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
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.equip.EquipmentChecking;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ElytraSlotFabricClientMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register(
        playerRenderState -> {
          ClientLevel level = Minecraft.getInstance().level;

          if (level != null) {
            Entity entity = level.getEntity(playerRenderState.id);

            if (entity instanceof LivingEntity livingEntity) {
              AccessoriesCapability cap = AccessoriesCapability.get(livingEntity);

              if (cap != null) {
                SlotEntryReference ref = cap.getFirstEquipped(s -> s.has(DataComponents.GLIDER),
                                                              EquipmentChecking.COSMETICALLY_OVERRIDABLE);

                return ref == null || ref.stack().isEmpty();
              }
            }
          }
          return true;
        });
    LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
        (entityType, entityRenderer, registrationHelper, context) -> {

          if (entityRenderer instanceof PlayerRenderer
              || entityRenderer instanceof ArmorStandRenderer) {
            registrationHelper.register(
                new ElytraSlotLayer<>(
                    (RenderLayerParent<HumanoidRenderState, EntityModel<HumanoidRenderState>>) entityRenderer,
                    context.getModelSet(), context.getEquipmentRenderer()));
          }
        });
  }
}
