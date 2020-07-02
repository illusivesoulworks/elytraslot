/*
 * Copyright (c) 2019-2020 C4
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

import java.util.UUID;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import top.theillusivec4.caelus.api.CaelusApi;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class CurioElytra implements ICurio {

  public static final AttributeModifier ELYTRA_CURIO_MODIFIER = new AttributeModifier(
      UUID.fromString("c754faef-9926-4a77-abbe-e34ef0d735aa"), "Elytra curio modifier", 1.0D,
      AttributeModifier.Operation.ADDITION);

  private ItemStack stack;

  public CurioElytra(ItemStack stack) {
    this.stack = stack;
  }

  @Override
  public void curioTick(String identifier, int index, LivingEntity entityLivingBase) {

    if (entityLivingBase.world.isRemote || !ElytraItem.isUsable(stack)) {
      return;
    }
    Integer ticksFlying = ObfuscationReflectionHelper
        .getPrivateValue(LivingEntity.class, entityLivingBase, "field_184629_bo");

    if (ticksFlying != null && (ticksFlying + 1) % 20 == 0) {
      stack.damageItem(1, entityLivingBase,
          entity -> entity.sendBreakAnimation(EquipmentSlotType.CHEST));
    }
  }

  @Override
  public boolean canEquip(String identifier, LivingEntity entityLivingBase) {
    return !(entityLivingBase.getItemStackFromSlot(EquipmentSlotType.CHEST)
        .getItem() instanceof ElytraItem) && !CuriosApi.getCuriosHelper()
        .findEquippedCurio(Items.ELYTRA, entityLivingBase).isPresent();
  }

  @Override
  public void onEquip(String identifier, int index, LivingEntity entityLivingBase) {
    ModifiableAttributeInstance attributeInstance = entityLivingBase
        .getAttribute(CaelusApi.ELYTRA_FLIGHT.get());

    if (attributeInstance != null && !attributeInstance.hasModifier(ELYTRA_CURIO_MODIFIER)
        && ElytraItem.isUsable(stack)) {
      attributeInstance.func_233767_b_(ELYTRA_CURIO_MODIFIER);
    }
  }

  @Override
  public void onUnequip(String identifier, int index, LivingEntity livingEntity) {
    ModifiableAttributeInstance attributeInstance = livingEntity
        .getAttribute(CaelusApi.ELYTRA_FLIGHT.get());

    if (attributeInstance != null) {
      attributeInstance.removeModifier(ELYTRA_CURIO_MODIFIER);
    }
  }

  @Override
  public void playRightClickEquipSound(LivingEntity livingEntity) {
    livingEntity.world.playSound(null, new BlockPos(livingEntity.getPositionVec()),
        SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA, SoundCategory.NEUTRAL, 1.0F, 1.0F);
  }

  @Override
  public boolean canRightClickEquip() {
    return true;
  }
}
