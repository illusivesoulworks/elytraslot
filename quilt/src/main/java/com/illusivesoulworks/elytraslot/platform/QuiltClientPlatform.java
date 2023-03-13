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

import com.illusivesoulworks.elytraslot.platform.services.IClientPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class QuiltClientPlatform implements IClientPlatform {

  @Override
  public boolean hasCustomCape(Player player) {
    return false;
  }

  @Override
  public ResourceLocation getCustomCape(Player player) {
    return null;
  }
}
