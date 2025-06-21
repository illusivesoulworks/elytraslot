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
import com.illusivesoulworks.caelus.api.RenderCapeEvent;
import com.illusivesoulworks.elytraslot.client.ElytraSlotLayer;
import java.util.Map;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

@Mod(value = ElytraSlotConstants.MOD_ID, dist = Dist.CLIENT)
public class ElytraSlotNeoForgeClientMod {

  public static final ContextKey<ItemStack> ELYTRA_RENDER = new ContextKey<>(
      ResourceLocation.fromNamespaceAndPath(ElytraSlotConstants.MOD_ID, "elytra_render"));

  public ElytraSlotNeoForgeClientMod(final IEventBus eventBus) {
    eventBus.addListener(this::addLayers);
    eventBus.addListener(this::elytraRenderState);
    NeoForge.EVENT_BUS.addListener(this::renderCape);
  }

  private void addLayers(final EntityRenderersEvent.AddLayers evt) {
    addEntityLayer(evt, EntityType.ARMOR_STAND);

    for (PlayerSkin.Model skin : evt.getSkins()) {
      addPlayerLayer(evt, skin);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static void addPlayerLayer(EntityRenderersEvent.AddLayers evt, PlayerSkin.Model skin) {
    EntityRenderer<? extends Player, ?> renderer = evt.getSkin(skin);

    if (renderer instanceof LivingEntityRenderer livingRenderer) {
      livingRenderer.addLayer(new ElytraSlotLayer(livingRenderer, evt.getEntityModels(),
                                                  evt.getContext().getEquipmentRenderer()));
    }
  }

  private static <T extends LivingEntity, R extends LivingEntityRenderer<T, ?, ?>> void addEntityLayer(
      EntityRenderersEvent.AddLayers evt, EntityType<? extends T> entityType) {
    R renderer = evt.getRenderer(entityType);

    if (renderer != null) {
      renderer.addLayer(new ElytraSlotLayer(renderer, evt.getEntityModels(), evt.getContext()
          .getEquipmentRenderer()));
    }
  }

  private void elytraRenderState(final RegisterRenderStateModifiersEvent evt) {
    evt.registerEntityModifier(
        new TypeToken<LivingEntityRenderer<? extends LivingEntity, LivingEntityRenderState, ?>>() {
        }, (livingEntity, renderState) -> {
          ICuriosItemHandler itemHandler = CuriosApi.getCuriosInventoryOrNull(livingEntity);

          if (itemHandler != null) {

            for (Map.Entry<String, ICurioStacksHandler> entry : itemHandler.getCurios()
                .entrySet()) {
              IDynamicStackHandler stacks = entry.getValue().getStacks();

              for (int i = 0; i < stacks.getSlots(); i++) {
                ItemStack stack = stacks.getStackInSlot(i);

                if (!stack.isEmpty() && stack.has(DataComponents.GLIDER) && entry.getValue()
                    .getRenders().get(i)) {
                  renderState.setRenderData(ELYTRA_RENDER, stack.copy());
                  return;
                }
              }
            }
            renderState.setRenderData(ELYTRA_RENDER, ItemStack.EMPTY);
          }
        });
  }

  private void renderCape(final RenderCapeEvent evt) {
    ItemStack stack =
        evt.getPlayerRenderState().getRenderDataOrDefault(ELYTRA_RENDER, ItemStack.EMPTY);

    if (!stack.isEmpty()) {
      evt.setCanceled(true);
    }
  }
}
