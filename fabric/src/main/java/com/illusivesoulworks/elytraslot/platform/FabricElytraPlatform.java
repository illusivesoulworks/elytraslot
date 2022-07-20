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
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import java.util.List;
import java.util.function.Function;
import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FabricElytraPlatform implements IElytraPlatform {

  @Override
  public boolean isEquipped(LivingEntity livingEntity) {
    return !getEquipped(livingEntity).isEmpty();
  }

  @Override
  public ItemStack getEquipped(LivingEntity livingEntity) {
    return TrinketsApi.getTrinketComponent(livingEntity)
        .map(trinketComponent -> {
          List<Tuple<SlotReference, ItemStack>> list =
              trinketComponent.getEquipped(ElytraSlotCommonMod.IS_ELYTRA);

          if (!list.isEmpty()) {
            return list.get(0).getB();
          }
          return ItemStack.EMPTY;
        })
        .orElse(ItemStack.EMPTY);
  }

  @Override
  public boolean canFly(ItemStack stack, LivingEntity livingEntity, boolean doTick) {
    Item item = stack.getItem();

    if (item instanceof ElytraItem) {
      return ElytraItem.isFlyEnabled(stack);
    } else if (item instanceof FabricElytraItem fabricElytraItem) {
      return fabricElytraItem.useCustomElytra(livingEntity, stack, doTick);
    }
    return false;
  }

  @Override
  public void processSlots(LivingEntity livingEntity, Function<ItemStack, Boolean> processor) {
    TrinketsApi.getTrinketComponent(livingEntity).ifPresent(trinketComponent -> {
      List<Tuple<SlotReference, ItemStack>> list = trinketComponent.getAllEquipped();

      for (Tuple<SlotReference, ItemStack> ref : list) {

        if (processor.apply(ref.getB())) {
          return;
        }
      }
    });
  }
}
