/*
 * Copyright (c) 2019-2021 C4
 *
 * This file is part of Elytra Trinket, a mod made for Minecraft.
 *
 * Elytra Trinket is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Elytra Trinket is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Elytra Trinket.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.elytratrinket;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import java.util.List;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.event.GameEvent;

public class ElytraTrinketMod implements ModInitializer {

  @Override
  public void onInitialize() {
    EntityElytraEvents.CUSTOM.register(this::useElytraTrinket);
  }

  private boolean useElytraTrinket(LivingEntity entity, boolean tickElytra) {
    return TrinketsApi.getTrinketComponent(entity).map(trinketComponent -> {
      List<Pair<SlotReference, ItemStack>> equipped = trinketComponent.getEquipped(
          stack -> stack.getItem() instanceof ElytraItem ||
              stack.getItem() instanceof FabricElytraItem);

      for (Pair<SlotReference, ItemStack> slot : equipped) {
        ItemStack stack = slot.getRight();
        Item item = stack.getItem();

        if (item instanceof ElytraItem) {

          if (ElytraItem.isUsable(stack)) {
            doVanillaElytraTick(entity, stack, slot.getLeft());
            return true;
          }
        } else if (item instanceof FabricElytraItem fabricElytraItem) {
          if (fabricElytraItem.useCustomElytra(entity, stack, tickElytra)) {
            return true;
          }
        }
      }
      return false;
    }).orElse(false);
  }

  private void doVanillaElytraTick(LivingEntity entity, ItemStack stack, SlotReference slotRef) {
    int nextRoll = entity.getRoll() + 1;

    if (!entity.world.isClient && nextRoll % 10 == 0) {

      if ((nextRoll / 10) % 2 == 0) {
        stack.damage(1, entity, p -> TrinketsApi.onTrinketBroken(stack, slotRef, entity));
      }
      entity.emitGameEvent(GameEvent.ELYTRA_GLIDE);
    }
  }
}
