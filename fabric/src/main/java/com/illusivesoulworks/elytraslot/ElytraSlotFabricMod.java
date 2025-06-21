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

import com.illusivesoulworks.elytraslot.common.AccessoryElytra;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoryRegistry;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import io.wispforest.accessories.api.slot.SlotPredicateRegistry;
import java.util.List;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ElytraSlotFabricMod implements ModInitializer {

  @Override
  public void onInitialize() {
    EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> {
      AccessoriesCapability cap = AccessoriesCapability.get(entity);

      if (cap != null && entity.level() instanceof ServerLevel serverLevel) {
        List<SlotEntryReference> entryReferences =
            cap.getEquipped(s -> s.has(DataComponents.GLIDER));

        if (!entryReferences.isEmpty()) {
          SlotEntryReference ref = Util.getRandom(entryReferences, entity.getRandom());
          ItemStack stack = ref.stack();

          if (!stack.isEmpty()) {

            if (tickElytra) {
              stack.hurtAndBreak(1, serverLevel,
                                 entity instanceof ServerPlayer serverPlayer ? serverPlayer : null,
                                 item -> ref.reference().breakStack());
            }
            return true;
          }
        }
      }
      return false;
    });
    SlotPredicateRegistry.register(
        ResourceLocation.fromNamespaceAndPath(ElytraSlotConstants.MOD_ID, "glider"),
        (level, slotType, index, stack) -> {

          if (stack.has(DataComponents.GLIDER)) {
            return TriState.TRUE;
          }
          return TriState.DEFAULT;
        });
    RegistryEntryAddedCallback.event(BuiltInRegistries.ITEM)
        .register((i, resourceLocation, item) -> {

          if (item.getDefaultInstance().has(DataComponents.GLIDER)) {
            AccessoryRegistry.register(item, new AccessoryElytra());
          }
        });
    for (Item item : BuiltInRegistries.ITEM) {

      if (item.getDefaultInstance().has(DataComponents.GLIDER)) {
        AccessoryRegistry.register(item, new AccessoryElytra());
      }
    }
  }
}
