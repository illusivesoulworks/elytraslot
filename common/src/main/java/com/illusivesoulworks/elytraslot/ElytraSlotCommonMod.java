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

import com.illusivesoulworks.elytraslot.client.ElytraRenderResult;
import com.illusivesoulworks.elytraslot.common.IElytraProvider;
import com.illusivesoulworks.elytraslot.common.VanillaElytraProvider;
import com.illusivesoulworks.elytraslot.platform.Services;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ElytraSlotCommonMod {

  public static final Predicate<ItemStack> IS_ELYTRA = new Predicate<>() {
    @Override
    public boolean test(ItemStack stack) {

      for (IElytraProvider provider : PROVIDERS) {

        if (provider.matches(stack)) {
          return true;
        }
      }
      return false;
    }
  };

  private static final List<IElytraProvider> PROVIDERS = new LinkedList<>();

  public static void init() {
    PROVIDERS.add(new VanillaElytraProvider());
  }

  public static Optional<ElytraRenderResult> getElytraRender(final LivingEntity livingEntity) {
    AtomicReference<ElytraRenderResult> result = new AtomicReference<>();
    Services.ELYTRA.processSlots(livingEntity, stack -> {

      if (!stack.isEmpty()) {

        for (IElytraProvider provider : PROVIDERS) {

          if (provider.matches(stack)) {
            result.set(provider.getRender(stack));
            return true;
          }
        }
      }
      return false;
    });
    return Optional.ofNullable(result.get());
  }

  public static boolean canFly(final LivingEntity livingEntity, boolean doTick) {
    AtomicBoolean result = new AtomicBoolean();
    Services.ELYTRA.processSlots(livingEntity, stack -> {

      if (!stack.isEmpty()) {

        for (IElytraProvider provider : PROVIDERS) {

          if (provider.matches(stack)) {
            result.set(provider.canFly(stack, livingEntity, doTick));
            return true;
          }
        }
      }
      return false;
    });
    return result.get();
  }

  public static boolean isEquipped(final LivingEntity livingEntity) {
    return Services.ELYTRA.isEquipped(livingEntity);
  }

  public static boolean canEquip(final LivingEntity livingEntity) {
    return !IS_ELYTRA.test(livingEntity.getItemBySlot(EquipmentSlot.CHEST)) &&
        !Services.ELYTRA.isEquipped(livingEntity);
  }
}