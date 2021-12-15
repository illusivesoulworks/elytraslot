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

package top.theillusivec4.curiouselytra.common;

import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

public class CurioElytra implements ICurio {

  public static final AttributeModifier ELYTRA_CURIO_MODIFIER =
      new AttributeModifier(UUID.fromString("c754faef-9926-4a77-abbe-e34ef0d735aa"),
          "Elytra curio modifier", 1.0D, AttributeModifier.Operation.ADDITION);

  private final ItemStack stack;

  public CurioElytra(ItemStack stack) {
    this.stack = stack;
  }

  @Override
  public ItemStack getStack() {
    return this.stack;
  }

  @Override
  public void curioTick(SlotContext slotContext) {
    LivingEntity livingEntity = slotContext.entity();
    int ticks = livingEntity.getFallFlyingTicks();

    if (ticks > 0 && livingEntity.isFallFlying()) {
      this.stack.elytraFlightTick(livingEntity, ticks);
    }
  }

  @Override
  public boolean canEquip(SlotContext slotContext) {
    LivingEntity livingEntity = slotContext.entity();
    ICuriosHelper curiosHelper = CuriosApi.getCuriosHelper();
    return !(livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ElytraItem) &&
        curiosHelper.findEquippedCurio(
            stack -> curiosHelper.getCurio(stack).map(curio -> curio instanceof CurioElytra)
                .orElse(false), livingEntity).isEmpty();
  }

  @Nonnull
  @Override
  public SoundInfo getEquipSound(SlotContext slotContext) {
    return new SoundInfo(SoundEvents.ARMOR_EQUIP_ELYTRA, 1.0F, 1.0F);
  }

  @Override
  public boolean canEquipFromUse(SlotContext slotContext) {
    return true;
  }
}
