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

package com.illusivesoulworks.elytraslot.platform;

import com.illusivesoulworks.elytraslot.ElytraSlotCommonMod;
import com.illusivesoulworks.elytraslot.platform.services.IElytraPlatform;
import java.util.function.Function;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class ForgeElytraPlatform implements IElytraPlatform {

  @Override
  public boolean isEquipped(final LivingEntity livingEntity) {
    return !getEquipped(livingEntity).isEmpty();
  }

  @Override
  public ItemStack getEquipped(final LivingEntity livingEntity) {
    return CuriosApi.getCuriosHelper().findFirstCurio(livingEntity, ElytraSlotCommonMod.IS_ELYTRA)
        .map(SlotResult::stack).orElse(ItemStack.EMPTY);
  }

  @Override
  public boolean canFly(ItemStack stack, LivingEntity livingEntity, boolean doTick) {
    return stack.canElytraFly(livingEntity);
  }

  @Override
  public void processSlots(LivingEntity livingEntity, Function<ItemStack, Boolean> processor) {
    CuriosApi.getCuriosHelper().getEquippedCurios(livingEntity).ifPresent(itemHandler -> {

      for (int i = 0; i < itemHandler.getSlots(); i++) {

        if (processor.apply(itemHandler.getStackInSlot(i))) {
          return;
        }
      }
    });
  }
}
