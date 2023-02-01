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

package com.illusivesoulworks.elytraslot.common;

import com.illusivesoulworks.elytraslot.client.ElytraRenderResult;
import com.illusivesoulworks.elytraslot.platform.Services;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SimpleCompatibilityProvider implements IElytraProvider {

  private static final Map<String, ResourceLocation> ID_TO_TEXTURE = new HashMap<>();
  private static boolean init = false;

  @Override
  public boolean matches(ItemStack stack) {

    if (!init) {
      Predicate<String> isLoaded = Services.PLATFORM::isModLoaded;

      if (isLoaded.test("deeperdarker")) {
        ID_TO_TEXTURE.put("deeperdarker:soul_elytra",
            new ResourceLocation("deeperdarker:textures/entity/soul_elytra.png"));
      }

      if (isLoaded.test("enderitemod")) {
        ID_TO_TEXTURE.put("enderitemod:enderite_elytra_seperated",
            new ResourceLocation("minecraft:textures/entity/enderite_elytra.png"));
      }

      if (isLoaded.test("mekanism")) {
        ID_TO_TEXTURE.put("mekanism:hdpe_elytra",
            new ResourceLocation("mekanism:textures/entity/hdpe_elytra.png"));
      }

      if (isLoaded.test("alexsmobs")) {
        ID_TO_TEXTURE.put("alexsmobs:tarantula_hawk_elytra", new ResourceLocation(""));
      }

      if (isLoaded.test("mana-and-artifice")) {
        ID_TO_TEXTURE.put("mana-and-artifice:spectral_elytra",
            new ResourceLocation("mana-and-artifice:textures/entity/elytra.png"));
      }

      if (isLoaded.test("netherelytra")) {
        ID_TO_TEXTURE.put("netherelytra:netherite_elytra",
            new ResourceLocation("netherelytra:textures/entity/netherite_elytra.png"));
      }

      if (isLoaded.test("lilwings")) {
        Set<ResourceLocation> entityTypes = Services.PLATFORM.getEntityTypes();

        for (ResourceLocation entityType : entityTypes) {

          if (entityType.getNamespace().equals("lilwings")) {
            String name = entityType.getPath().substring(0, entityType.getPath().lastIndexOf("_")) +
                "_elytra";
            ID_TO_TEXTURE.put("lilwings:" + name,
                new ResourceLocation("lilwings", "textures/elytra/" + name + ".png"));
          }
        }
      }
      init = true;
    }
    return ID_TO_TEXTURE.containsKey(Services.PLATFORM.getId(stack.getItem()).toString());
  }

  @Override
  public ElytraRenderResult getRender(ItemStack stack) {
    return new ElytraRenderResult(COLOR,
        ID_TO_TEXTURE.get(Services.PLATFORM.getId(stack.getItem()).toString()),
        stack.isEnchanted(), stack, hasCapeTexture(stack));
  }

  @Override
  public boolean hasCapeTexture(ItemStack stack) {
    ResourceLocation rl = Services.PLATFORM.getId(stack.getItem());

    if (rl != null) {

      if (Services.PLATFORM.isModLoaded("deeperdarker") &&
          rl.toString().equals("deeperdarker:soul_elytra")) {
        return false;
      } else if (Services.PLATFORM.isModLoaded("lilwings") &&
          rl.getNamespace().equals("lilwings")) {
        return false;
      }
    }
    return true;
  }
}
