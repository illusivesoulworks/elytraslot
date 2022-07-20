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
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.awt.Color;
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

      if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {

        if (abstractclientplayer.isElytraLoaded() &&
            abstractclientplayer.getElytraTextureLocation() != null) {
          resourcelocation = abstractclientplayer.getElytraTextureLocation();
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
      poseStack.pushPose();
      poseStack.translate(0.0D, 0.0D, 0.125D);
      this.getParentModel().copyPropertiesTo(this.elytraModel);
      this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks,
          netHeadYaw, headPitch);
      VertexConsumer vertexconsumer =
          ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(resourcelocation),
              false, elytra.enchanted());
      Color color = elytra.color();
      float red = color.getRed() / 255.0F;
      float green = color.getGreen() / 255.0F;
      float blue = color.getBlue() / 255.0F;
      float alpha = color.getAlpha() / 255.0F;
      this.elytraModel.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY,
          red, green, blue, alpha);
      poseStack.popPose();
    });
  }
}
