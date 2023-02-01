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

import com.illusivesoulworks.elytraslot.client.ElytraSlotLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.caelus.api.RenderCapeEvent;

public class ElytraSlotForgeClientMod {

  public static void setup() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(ElytraSlotForgeClientMod::addLayers);
    MinecraftForge.EVENT_BUS.addListener(ElytraSlotForgeClientMod::renderCape);
  }

  private static void addLayers(final EntityRenderersEvent.AddLayers evt) {
    addPlayerLayer(evt, "default");
    addPlayerLayer(evt, "slim");
    addEntityLayer(evt, EntityType.ARMOR_STAND);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static void addPlayerLayer(EntityRenderersEvent.AddLayers evt, String skin) {
    EntityRenderer<? extends Player> renderer = evt.getSkin(skin);
    boolean slim = skin.equals("slim");

    if (renderer instanceof LivingEntityRenderer livingRenderer) {
      livingRenderer.addLayer(new ElytraSlotLayer(livingRenderer, evt.getEntityModels()));
      livingRenderer.addLayer(
          new ElytraSlotArmorLayer(livingRenderer, evt.getEntityModels(), slim));
    }
  }

  private static <T extends LivingEntity, M extends HumanoidModel<T>, R extends LivingEntityRenderer<T, M>> void addEntityLayer(
      EntityRenderersEvent.AddLayers evt, EntityType<? extends T> entityType) {
    R renderer = evt.getRenderer(entityType);

    if (renderer != null) {
      renderer.addLayer(new ElytraSlotLayer<>(renderer, evt.getEntityModels()));
      renderer.addLayer(new ElytraSlotArmorLayer<>(renderer, evt.getEntityModels(), false));
    }
  }

  private static void renderCape(final RenderCapeEvent evt) {

    if (ElytraSlotCommonMod.isEquipped(evt.getEntity())) {
      evt.setCanceled(true);
    }
  }
}
