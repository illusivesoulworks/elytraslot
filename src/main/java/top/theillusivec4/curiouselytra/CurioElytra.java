/*
 * Copyright (C) 2019  C4
 *
 * This file is part of Curious Elytra, a mod made for Minecraft.
 *
 * Curious Elytra is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Curious Elytra is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Curious Elytra.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.curiouselytra;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.model.ModelElytra;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import top.theillusivec4.caelus.api.CaelusAPI;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.ICurio;

import java.util.UUID;

public class CurioElytra implements ICurio {

    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("minecraft:textures/entity/elytra.png");

    public static final AttributeModifier ELYTRA_CURIO_MODIFIER = new AttributeModifier(UUID.fromString("c754faef-9926-4a77-abbe-e34ef0d735aa"), "Elytra curio modifier", 1.0D, 0);

    private ItemStack stack;
    private Object model;

    public CurioElytra(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void onCurioTick(String identifier, EntityLivingBase entityLivingBase) {

        if (!entityLivingBase.world.isRemote && ItemElytra.isUsable(stack)) {
            int ticksFlying = ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, entityLivingBase, "field_184629_bo");

            if ((ticksFlying + 1) % 20 == 0) {
                stack.damageItem(1, entityLivingBase);
            }
        }
    }

    @Override
    public boolean canEquip(String identifier, EntityLivingBase entityLivingBase) {
        return entityLivingBase.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA &&
                CuriosAPI.getCurioEquipped(Items.ELYTRA, entityLivingBase) == null;
    }

    @Override
    public void onEquipped(String identifier, EntityLivingBase entityLivingBase) {
        IAttributeInstance attributeInstance = entityLivingBase.getAttribute(CaelusAPI.ELYTRA_FLIGHT);

        if (!attributeInstance.hasModifier(ELYTRA_CURIO_MODIFIER) && ItemElytra.isUsable(stack)) {
            attributeInstance.applyModifier(ELYTRA_CURIO_MODIFIER);
        }
    }

    @Override
    public void onUnequipped(String identifier, EntityLivingBase entityLivingBase) {
        entityLivingBase.getAttribute(CaelusAPI.ELYTRA_FLIGHT).removeModifier(ELYTRA_CURIO_MODIFIER);
    }

    @Override
    public void playEquipSound(EntityLivingBase entityLivingBase) {
        entityLivingBase.world.playSound(null, entityLivingBase.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

    @Override
    public boolean canRightClickEquip() {
        return true;
    }

    @Override
    public boolean hasRender(String identifier, EntityLivingBase entityLivingBase) {
        return true;
    }

    @Override
    public void doRender(String identifier, EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        TextureManager textureManager = Minecraft.getInstance().textureManager;
        if (entitylivingbaseIn instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entitylivingbaseIn;
            if (abstractclientplayer.isPlayerInfoSet() && abstractclientplayer.getLocationElytra() != null) {
                textureManager.bindTexture(abstractclientplayer.getLocationElytra());
            } else if (abstractclientplayer.hasPlayerInfo() && abstractclientplayer.getLocationCape() != null && abstractclientplayer.isWearing(EnumPlayerModelParts.CAPE)) {
                textureManager.bindTexture(abstractclientplayer.getLocationCape());
            } else {
                textureManager.bindTexture(TEXTURE_ELYTRA);
            }
        } else {
            textureManager.bindTexture(TEXTURE_ELYTRA);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translatef(0.0F, 0.0F, 0.125F);

        if (!(this.model instanceof ModelElytra)) {
            this.model = new ModelElytra();
        }
        ModelElytra modelElytra = (ModelElytra) this.model;
        modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
        modelElytra.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        if (stack.isEnchanted()) {
            Render render = Minecraft.getInstance().getRenderManager().getEntityRenderObject(entitylivingbaseIn);

            if (render instanceof RenderLivingBase) {
                LayerArmorBase.renderEnchantedGlint((RenderLivingBase) render, entitylivingbaseIn, modelElytra, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
