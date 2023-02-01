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

package com.illusivesoulworks.elytraslot.platform;

import com.illusivesoulworks.elytraslot.platform.services.IPlatform;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgePlatform implements IPlatform {

  private static final Map<String, Boolean> CACHE = new HashMap<>();

  @Override
  public boolean isModLoaded(String id) {
    return CACHE.computeIfAbsent(id, (input) -> ModList.get().isLoaded(input));
  }

  @Override
  public ResourceLocation getId(Item item) {
    return ForgeRegistries.ITEMS.getKey(item);
  }

  @Nonnull
  @Override
  public Set<ResourceLocation> getEntityTypes() {
    return ForgeRegistries.ENTITY_TYPES.getKeys();
  }
}
