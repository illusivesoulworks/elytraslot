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

import com.illusivesoulworks.caelus.api.CaelusApi;
import com.illusivesoulworks.caelus.api.GlidingDamageEvent;
import com.illusivesoulworks.elytraslot.common.CurioElytra;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.CuriosSlotTypes;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

@Mod(ElytraSlotConstants.MOD_ID)
public class ElytraSlotNeoForgeMod {

  public ElytraSlotNeoForgeMod(IEventBus eventBus) {
    eventBus.addListener(this::setup);
    eventBus.addListener(this::registerCapabilities);
    NeoForge.EVENT_BUS.addListener(this::glidingDamage);
  }

  private void setup(final FMLCommonSetupEvent evt) {
    NeoForge.EVENT_BUS.addListener(this::playerTick);
    CuriosSlotTypes.registerPredicate(
        ResourceLocation.fromNamespaceAndPath(ElytraSlotConstants.MOD_ID, "glider"),
        (slotContext, stack) -> stack.has(DataComponents.GLIDER));
  }

  private void glidingDamage(final GlidingDamageEvent evt) {
    ICuriosItemHandler curios = CuriosApi.getCuriosInventoryOrNull(evt.getEntity());

    if (curios != null) {

      for (SlotResult curio : curios.findCurios(stack -> stack.has(DataComponents.GLIDER))) {
        evt.addGlider(curio.stack(),
                      item -> CuriosApi.broadcastCurioBreakEvent(curio.slotContext()));
      }
    }
  }

  private void playerTick(final PlayerTickEvent.Post evt) {
    Player player = evt.getEntity();
    AttributeInstance attributeInstance =
        player.getAttribute(CaelusApi.getInstance().getFallFlyingAttribute());

    if (attributeInstance != null) {
      attributeInstance.removeModifier(CurioElytra.ELYTRA_CURIO_MODIFIER.id());

      if (!attributeInstance.hasModifier(CurioElytra.ELYTRA_CURIO_MODIFIER.id()) &&
          CuriosApi.getCuriosInventory(player)
              .map(curios -> curios.isEquipped(stack -> stack.has(DataComponents.GLIDER)))
              .orElse(false)) {
        attributeInstance.addTransientModifier(CurioElytra.ELYTRA_CURIO_MODIFIER);
      }
    }
  }

  private void registerCapabilities(final RegisterCapabilitiesEvent evt) {

    for (Item item : BuiltInRegistries.ITEM) {

      if (item.getDefaultInstance().has(DataComponents.GLIDER)) {
        evt.registerItem(CuriosCapability.ITEM, (stack, context) -> new CurioElytra(stack), item);
      }
    }
  }
}