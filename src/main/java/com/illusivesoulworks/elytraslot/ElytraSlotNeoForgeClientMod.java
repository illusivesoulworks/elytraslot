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

import com.google.common.reflect.TypeToken;
import com.illusivesoulworks.elytraslot.client.ElytraSlotLayer;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

@Mod(value = ElytraSlotConstants.MOD_ID, dist = Dist.CLIENT)
public class ElytraSlotNeoForgeClientMod {

  public static final ContextKey<ItemStack> ELYTRA_RENDER = new ContextKey<>(
      Identifier.fromNamespaceAndPath(ElytraSlotConstants.MOD_ID, "elytra_render"));

  public ElytraSlotNeoForgeClientMod(IEventBus eventBus) {
    eventBus.addListener(this::addLayers);
    eventBus.addListener(this::elytraRenderState);
  }

  private void addLayers(EntityRenderersEvent.AddLayers event) {
    ArmorStandRenderer stand = event.getRenderer(EntityType.ARMOR_STAND);
    if (stand != null) {
      stand.addLayer(new ElytraSlotLayer<>(stand, event.getEntityModels(),
          event.getContext().getEquipmentRenderer()));
    }
    for (PlayerModelType skin : event.getSkins()) {
      var renderer = event.getPlayerRenderer(skin);
      if (renderer != null) {
        renderer.addLayer(new ElytraSlotLayer<>(renderer, event.getEntityModels(),
            event.getContext().getEquipmentRenderer()));
      }
    }
  }

  private void elytraRenderState(RegisterRenderStateModifiersEvent event) {
    event.registerEntityModifier(
        new TypeToken<LivingEntityRenderer<? extends LivingEntity, LivingEntityRenderState, ?>>() {},
        (entity, state) -> state.setRenderData(ELYTRA_RENDER, visibleGlider(entity)));
  }

  private static ItemStack visibleGlider(LivingEntity entity) {
    ICuriosItemHandler inventory = CuriosApi.getCuriosInventoryOrNull(entity);
    if (inventory == null) {
      return ItemStack.EMPTY;
    }
    for (ICurioStacksHandler handler : inventory.getCurios().values()) {
      IDynamicStackHandler stacks = handler.getStacks();
      for (int index = 0; index < stacks.getSlots(); index++) {
        ItemStack stack = stacks.getStackInSlot(index);
        if (stack.has(DataComponents.GLIDER) && handler.getRenders().get(index)) {
          return stack.copy();
        }
      }
    }
    return ItemStack.EMPTY;
  }
}
