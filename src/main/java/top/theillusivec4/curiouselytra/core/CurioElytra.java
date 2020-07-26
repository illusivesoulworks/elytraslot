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

package top.theillusivec4.curiouselytra.core;

import java.util.UUID;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import top.theillusivec4.caelus.api.CaelusApi;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.component.ICurio;

public class CurioElytra implements ICurio {

  public static final EntityAttributeModifier ELYTRA_CURIO_MODIFIER = new EntityAttributeModifier(
      UUID.fromString("c754faef-9926-4a77-abbe-e34ef0d735aa"), "Elytra curio modifier", 1.0D,
      EntityAttributeModifier.Operation.ADDITION);

  private ItemStack stack;

  public CurioElytra(ItemStack stack) {
    this.stack = stack;
  }

  @Override
  public void curioTick(String identifier, int index, LivingEntity entityLivingBase) {

    if (entityLivingBase.getEntityWorld().isClient() || !ElytraItem.isUsable(stack)) {
      return;
    }
    int ticksFlying = CuriousElytra.getAccessor().getFlyingTicks(entityLivingBase);

    if ((ticksFlying + 1) % 20 == 0) {
      stack.damage(1, entityLivingBase,
          entity -> entity.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
    }
  }

  @Override
  public boolean canEquip(String identifier, LivingEntity entityLivingBase) {
    return !(entityLivingBase.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ElytraItem)
        && !CuriosApi.getCuriosHelper().findEquippedCurio(Items.ELYTRA, entityLivingBase)
        .isPresent();
  }

  @Override
  public void onEquip(String identifier, int index, LivingEntity entityLivingBase) {
    EntityAttributeInstance attributeInstance = entityLivingBase
        .getAttributeInstance(CaelusApi.ELYTRA_FLIGHT);

    if (attributeInstance != null && !attributeInstance.hasModifier(ELYTRA_CURIO_MODIFIER)
        && ElytraItem.isUsable(stack)) {
      attributeInstance.addTemporaryModifier(ELYTRA_CURIO_MODIFIER);
    }
  }

  @Override
  public void onUnequip(String identifier, int index, LivingEntity livingEntity) {
    EntityAttributeInstance attributeInstance = livingEntity
        .getAttributeInstance(CaelusApi.ELYTRA_FLIGHT);

    if (attributeInstance != null) {
      attributeInstance.removeModifier(ELYTRA_CURIO_MODIFIER);
    }
  }

  @Override
  public void playRightClickEquipSound(LivingEntity livingEntity) {
    livingEntity.world
        .playSound(null, livingEntity.getBlockPos(), SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA,
            SoundCategory.NEUTRAL, 1.0F, 1.0F);
  }

  @Override
  public boolean canRightClickEquip() {
    return true;
  }
}
