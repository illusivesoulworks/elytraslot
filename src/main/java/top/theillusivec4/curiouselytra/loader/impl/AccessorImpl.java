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

package top.theillusivec4.curiouselytra.loader.impl;

import net.minecraft.entity.LivingEntity;
import top.theillusivec4.curiouselytra.core.Accessor;
import top.theillusivec4.curiouselytra.loader.mixin.LivingEntityAccessor;

public class AccessorImpl implements Accessor {

  @Override
  public int getFlyingTicks(LivingEntity livingEntity) {
    return ((LivingEntityAccessor) livingEntity).getRoll();
  }
}
