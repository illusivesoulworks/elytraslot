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

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

public class ElytraSlotArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>>
    extends
    RenderLayer<T, M> {

  private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
  private final A innerModel;
  private final A outerModel;

  public ElytraSlotArmorLayer(RenderLayerParent<T, M> pRenderer, EntityModelSet modelSet,
                              boolean slim) {
    super(pRenderer);
    this.innerModel =
        (A) (slim ? new HumanoidModel(modelSet.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)) :
            new HumanoidModel(modelSet.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)));
    this.outerModel =
        (A) (slim ? new HumanoidModel(modelSet.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR)) :
            new HumanoidModel(modelSet.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)));
  }

  public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight,
                     T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks,
                     float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    ElytraSlotCommonMod.getElytraRender(pLivingEntity).ifPresent(elytra -> {
      ItemStack stack = elytra.stack();

      if (elytra.stack().getItem() instanceof ArmorItem armoritem) {
        EquipmentSlot slot = EquipmentSlot.CHEST;

        if (armoritem.getEquipmentSlot() == slot) {
          A originalModel = this.getArmorModel(slot);
          this.getParentModel().copyPropertiesTo(originalModel);
          this.setPartVisibility(originalModel, slot);
          Model model = getArmorModelHook(pLivingEntity, elytra.stack(), slot, originalModel);
          boolean flag1 = stack.hasFoil();
          if (armoritem instanceof DyeableLeatherItem) {
            int i = ((DyeableLeatherItem) armoritem).getColor(stack);
            float f = (float) (i >> 16 & 255) / 255.0F;
            float f1 = (float) (i >> 8 & 255) / 255.0F;
            float f2 = (float) (i & 255) / 255.0F;
            this.renderModel(pMatrixStack, pBuffer, pPackedLight, flag1, model, f, f1, f2,
                this.getArmorResource(pLivingEntity, stack, slot, null));
            this.renderModel(pMatrixStack, pBuffer, pPackedLight, flag1, model, 1.0F, 1.0F, 1.0F,
                this.getArmorResource(pLivingEntity, stack, slot, "overlay"));
          } else {
            this.renderModel(pMatrixStack, pBuffer, pPackedLight, flag1, model, 1.0F, 1.0F, 1.0F,
                this.getArmorResource(pLivingEntity, stack, slot, null));
          }
        }
      }
    });
  }

  protected void setPartVisibility(A pModel, EquipmentSlot pSlot) {
    pModel.setAllVisible(false);
    switch (pSlot) {
      case HEAD:
        pModel.head.visible = true;
        pModel.hat.visible = true;
        break;
      case CHEST:
        pModel.body.visible = true;
        pModel.rightArm.visible = true;
        pModel.leftArm.visible = true;
        break;
      case LEGS:
        pModel.body.visible = true;
        pModel.rightLeg.visible = true;
        pModel.leftLeg.visible = true;
        break;
      case FEET:
        pModel.rightLeg.visible = true;
        pModel.leftLeg.visible = true;
    }

  }

  private void renderModel(PoseStack pPoseStack, MultiBufferSource pBuffer, int p_117109_,
                           boolean p_117111_, net.minecraft.client.model.Model pModel,
                           float p_117114_, float p_117115_, float p_117116_,
                           ResourceLocation armorResource) {
    VertexConsumer vertexconsumer =
        ItemRenderer.getArmorFoilBuffer(pBuffer, RenderType.armorCutoutNoCull(armorResource), false,
            p_117111_);
    pModel.renderToBuffer(pPoseStack, vertexconsumer, p_117109_, OverlayTexture.NO_OVERLAY,
        p_117114_, p_117115_, p_117116_, 1.0F);
  }

  private A getArmorModel(EquipmentSlot pSlot) {
    return this.usesInnerModel(pSlot) ? this.innerModel : this.outerModel;
  }

  private boolean usesInnerModel(EquipmentSlot pSlot) {
    return pSlot == EquipmentSlot.LEGS;
  }

  protected Model getArmorModelHook(T entity, ItemStack itemStack, EquipmentSlot slot, A model) {
    return ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
  }

  public ResourceLocation getArmorResource(net.minecraft.world.entity.Entity entity,
                                           ItemStack stack, EquipmentSlot slot,
                                           @Nullable String type) {
    ArmorItem item = (ArmorItem) stack.getItem();
    String texture = item.getMaterial().getName();
    String domain = "minecraft";
    int idx = texture.indexOf(':');
    if (idx != -1) {
      domain = texture.substring(0, idx);
      texture = texture.substring(idx + 1);
    }
    String s1 =
        String.format(java.util.Locale.ROOT, "%s:textures/models/armor/%s_layer_%d%s.png", domain,
            texture, (usesInnerModel(slot) ? 2 : 1),
            type == null ? "" : String.format(java.util.Locale.ROOT, "_%s", type));

    s1 = ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
    ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);

    if (resourcelocation == null) {
      resourcelocation = new ResourceLocation(s1);
      ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
    }

    return resourcelocation;
  }
}
