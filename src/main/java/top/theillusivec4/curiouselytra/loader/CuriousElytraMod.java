/*
 * Copyright (c) 2019-2020 C4
 *
 * This file is part of Curious Elytra, a mod made for Minecraft.
 *
 * Curious Elytra is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Curious Elytra is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Curious Elytra.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.curiouselytra.loader;

import nerdhub.cardinal.components.api.event.ItemComponentCallbackV2;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosComponent;
import top.theillusivec4.curios.api.SlotTypeInfo.BuildScheme;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curiouselytra.core.CurioElytra;
import top.theillusivec4.curiouselytra.loader.integration.NetheritePlusIntegration;

public class CuriousElytraMod implements ModInitializer {

  @Override
  public void onInitialize() {
    CuriosApi.enqueueSlotType(BuildScheme.REGISTER, SlotTypePreset.BACK.getInfoBuilder().build());
    boolean isNetheriteLoaded = FabricLoader.getInstance().isModLoaded("netherite_plus");

    for (Item item : Registry.ITEM) {

      if (item instanceof ElytraItem ||
          (isNetheriteLoaded && NetheritePlusIntegration.isNetheriteElytra(item))) {
        registerComponent(item);
      }
    }
    RegistryEntryAddedCallback.event(Registry.ITEM).register((rawId, id, item) -> {

      if (item instanceof ElytraItem ||
          (isNetheriteLoaded && NetheritePlusIntegration.isNetheriteElytra(id))) {
        registerComponent(item);
      }
    });
  }

  private static void registerComponent(Item itemIn) {
    ItemComponentCallbackV2.event(itemIn).register(
        ((item, itemStack, componentContainer) -> componentContainer
            .put(CuriosComponent.ITEM, new CurioElytra(itemStack))));
  }
}
