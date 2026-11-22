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

import com.illusivesoulworks.elytraslot.common.CurioElytra;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.CuriosSlotTypes;
import top.theillusivec4.curios.api.SlotResult;

@Mod(ElytraSlotConstants.MOD_ID)
public class ElytraSlotNeoForgeMod {

  public ElytraSlotNeoForgeMod(IEventBus eventBus) {
    eventBus.addListener(this::setup);
    eventBus.addListener(this::registerCapabilities);
  }

  private void setup(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> CuriosSlotTypes.registerPredicate(
        Identifier.fromNamespaceAndPath(ElytraSlotConstants.MOD_ID, "glider"),
        (slot, stack) -> stack.has(DataComponents.GLIDER)));
  }

  public static boolean canGlideUsing(ItemStack stack) {
    return stack.has(DataComponents.GLIDER) && !stack.nextDamageWillBreak();
  }

  public static List<SlotResult> findGliders(Player player) {
    return CuriosApi.getCuriosInventory(player)
        .map(inventory -> inventory.findCurios(ElytraSlotNeoForgeMod::canGlideUsing))
        .orElseGet(List::of);
  }

  private void registerCapabilities(RegisterCapabilitiesEvent event) {
    for (Item item : BuiltInRegistries.ITEM) {
      if (item.getDefaultInstance().has(DataComponents.GLIDER)) {
        event.registerItem(CuriosCapability.ITEM, (stack, context) -> new CurioElytra(stack), item);
      }
    }
  }
}
