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

package com.illusivesoulworks.elytraslot.client;

import com.illusivesoulworks.elytraslot.ElytraSlotNeoForgeClientMod;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.equipment.ElytraModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;

public class ElytraSlotLayer<S extends HumanoidRenderState, M extends EntityModel<S>>
    extends RenderLayer<S, M> {

  private final ElytraModel elytraModel;
  private final ElytraModel elytraBabyModel;
  private final EquipmentLayerRenderer equipmentRenderer;

  public ElytraSlotLayer(RenderLayerParent<S, M> parent, EntityModelSet models,
                         EquipmentLayerRenderer equipmentRenderer) {
    super(parent);
    this.elytraModel = new ElytraModel(models.bakeLayer(ModelLayers.ELYTRA));
    this.elytraBabyModel = new ElytraModel(models.bakeLayer(ModelLayers.ELYTRA_BABY));
    this.equipmentRenderer = equipmentRenderer;
  }

  public static ItemStack getGlider(HumanoidRenderState state) {
    if (state.chestEquipment.has(DataComponents.GLIDER)) {
      return ItemStack.EMPTY;
    }
    ItemStack stack = state.getRenderDataOrDefault(ElytraSlotNeoForgeClientMod.ELYTRA_RENDER,
        ItemStack.EMPTY);
    Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
    return equippable != null && equippable.assetId().isPresent() ? stack : ItemStack.EMPTY;
  }

  @Override
  public void submit(PoseStack poses, SubmitNodeCollector collector, int light,
                     S state, float yaw, float pitch) {
    ItemStack stack = getGlider(state);
    Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
    if (equippable == null) {
      return;
    }
    poses.pushPose();
    poses.translate(0.0F, 0.0F, 0.125F);
    equipmentRenderer.renderLayers(EquipmentClientInfo.LayerType.WINGS, equippable.assetId().get(),
        state.isBaby ? elytraBabyModel : elytraModel, state, stack, poses, collector, light,
        getPlayerElytraTexture(state), state.outlineColor, 0);
    poses.popPose();
  }

  private static Identifier getPlayerElytraTexture(HumanoidRenderState state) {
    if (state instanceof AvatarRenderState avatar) {
      PlayerSkin skin = avatar.skin;
      if (skin.elytra() != null) {
        return skin.elytra().texturePath();
      }
      if (skin.cape() != null && avatar.showCape) {
        return skin.cape().texturePath();
      }
    }
    return null;
  }
}
