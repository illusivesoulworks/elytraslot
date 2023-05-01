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

import com.illusivesoulworks.elytraslot.ElytraSlotCommonMod;
import com.illusivesoulworks.elytraslot.platform.Services;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nonnull;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ArmorItem;

public class ElytraSlotLayer<T extends LivingEntity, M extends EntityModel<T>>
    extends RenderLayer<T, M> {

  private final ElytraModel<T> elytraModel;

  public ElytraSlotLayer(RenderLayerParent<T, M> layerParent, EntityModelSet modelSet) {
    super(layerParent);
    this.elytraModel = new ElytraModel<>(modelSet.bakeLayer(ModelLayers.ELYTRA));
  }

  public void render(@Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer,
                     int light, @Nonnull T livingEntity, float limbSwing, float limbSwingAmount,
                     float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
    ElytraSlotCommonMod.getElytraRender(livingEntity).ifPresent(elytra -> {
      ResourceLocation resourcelocation;

      if (elytra.stack().getItem() instanceof ArmorItem) {
        return;
      }

      if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {

        if (abstractclientplayer.isElytraLoaded() &&
            abstractclientplayer.getElytraTextureLocation() != null) {
          resourcelocation = abstractclientplayer.getElytraTextureLocation();
        } else if (elytra.useCapeTexture()) {

          if (Services.PLATFORM.isModLoaded("minecraftcapes") &&
              Services.CLIENT.hasCustomCape(abstractclientplayer)) {
            resourcelocation = Services.CLIENT.getCustomCape(abstractclientplayer);
          } else if (abstractclientplayer.isCapeLoaded() &&
              abstractclientplayer.getCloakTextureLocation() != null &&
              abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
            resourcelocation = abstractclientplayer.getCloakTextureLocation();
          } else {
            resourcelocation = elytra.texture();
          }
        } else {
          resourcelocation = elytra.texture();
        }
      } else {
        resourcelocation = elytra.texture();
      }
      poseStack.pushPose();
      poseStack.translate(0.0D, 0.0D, 0.125D);
      this.getParentModel().copyPropertiesTo(this.elytraModel);
      this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks,
          netHeadYaw, headPitch);
      VertexConsumer vertexconsumer =
          ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(resourcelocation),
              false, elytra.enchanted());
      ElytraColor color = elytra.color();
      this.elytraModel.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY,
          color.red(), color.green(), color.blue(), color.alpha());
      poseStack.popPose();
    });
  }
}
