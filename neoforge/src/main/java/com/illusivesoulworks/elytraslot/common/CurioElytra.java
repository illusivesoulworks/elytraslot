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

package com.illusivesoulworks.elytraslot.common;

import com.illusivesoulworks.elytraslot.ElytraSlotConstants;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class CurioElytra implements ICurio {

  public static final AttributeModifier ELYTRA_CURIO_MODIFIER = new AttributeModifier(
      ResourceLocation.fromNamespaceAndPath(ElytraSlotConstants.MOD_ID, "elytra"), 1.0D,
      AttributeModifier.Operation.ADD_VALUE);

  private final ItemStack stack;

  public CurioElytra(ItemStack stack) {
    this.stack = stack;
  }

  @Override
  public ItemStack getStack() {
    return this.stack;
  }

  @Override
  public boolean canEquip(SlotContext slotContext) {
    return this.stack.getItem() != slotContext.entity().getItemBySlot(EquipmentSlot.CHEST)
        .getItem();
  }

  @Nonnull
  @Override
  public SoundInfo getEquipSound(SlotContext slotContext) {
    return new SoundInfo(SoundEvents.ARMOR_EQUIP_ELYTRA.value(), 1.0F, 1.0F);
  }

  @Override
  public boolean canEquipFromUse(SlotContext slotContext) {
    return true;
  }
}
