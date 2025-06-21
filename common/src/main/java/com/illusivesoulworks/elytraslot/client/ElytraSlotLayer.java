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

import com.illusivesoulworks.elytraslot.integration.IntegrationConstants;
import com.illusivesoulworks.elytraslot.integration.minecraftcapes.MinecraftCapesPlugin;
import com.illusivesoulworks.elytraslot.platform.ClientServices;
import com.illusivesoulworks.elytraslot.platform.Services;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.Equippable;

public class ElytraSlotLayer<S extends HumanoidRenderState, M extends EntityModel<S>>
    extends RenderLayer<S, M> {

  private final ElytraModel elytraModel;
  private final ElytraModel elytraBabyModel;
  private final EquipmentLayerRenderer equipmentRenderer;

  public ElytraSlotLayer(RenderLayerParent<S, M> renderer, EntityModelSet models,
                         EquipmentLayerRenderer equipmentRenderer) {
    super(renderer);
    this.elytraModel = new ElytraModel(models.bakeLayer(ModelLayers.ELYTRA));
    this.elytraBabyModel = new ElytraModel(models.bakeLayer(ModelLayers.ELYTRA_BABY));
    this.equipmentRenderer = equipmentRenderer;
  }

  public void render(@Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource,
                     int packedLight, @Nonnull S renderState, float p_371865_, float p_371528_) {
    ItemStack elytra = ClientServices.CLIENT.getRenderingElytra(renderState);

    if (!elytra.isEmpty() && renderState.chestEquipment.getItem() != Items.ELYTRA) {
      Equippable equippable = elytra.get(DataComponents.EQUIPPABLE);

      if (equippable != null && equippable.assetId().isPresent()) {
        ResourceLocation resourcelocation = getPlayerElytraTexture(renderState);
        ElytraModel elytramodel = renderState.isBaby ? this.elytraBabyModel : this.elytraModel;
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 0.125F);
        elytramodel.setupAnim(renderState);
        this.equipmentRenderer
            .renderLayers(
                EquipmentClientInfo.LayerType.WINGS, equippable.assetId().get(), elytramodel,
                elytra, poseStack, bufferSource, packedLight, resourcelocation
            );
        poseStack.popPose();
      }
    }
  }

  @Nullable
  private static ResourceLocation getPlayerElytraTexture(HumanoidRenderState renderState) {

    if (renderState instanceof PlayerRenderState playerrenderstate) {
      PlayerSkin playerskin = playerrenderstate.skin;

      if (playerskin.elytraTexture() != null) {
        return playerskin.elytraTexture();
      }

      if (Services.SERVER.isModLoaded(IntegrationConstants.MINECRAFT_CAPES)) {
        ResourceLocation resourceLocation = MinecraftCapesPlugin.getCapeLocation(playerrenderstate);

        if (resourceLocation != null) {
          return resourceLocation;
        }
      }

      if (playerskin.capeTexture() != null && playerrenderstate.showCape) {
        return playerskin.capeTexture();
      }
    }
    return null;
  }
}
