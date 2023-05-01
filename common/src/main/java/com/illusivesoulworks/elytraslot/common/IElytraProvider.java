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

import com.illusivesoulworks.elytraslot.client.ElytraColor;
import com.illusivesoulworks.elytraslot.client.ElytraRenderResult;
import com.illusivesoulworks.elytraslot.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IElytraProvider {

  ResourceLocation TEXTURE = new ResourceLocation("minecraft:textures/entity/elytra.png");
  ElytraColor COLOR = new ElytraColor(1.0F, 1.0F, 1.0F, 1.0F);

  boolean matches(ItemStack stack);

  default ElytraRenderResult getRender(ItemStack stack) {
    return new ElytraRenderResult(COLOR, TEXTURE, stack.isEnchanted(), stack,
        hasCapeTexture(stack));
  }

  default boolean canFly(ItemStack stack, LivingEntity livingEntity, boolean doTick) {
    return Services.ELYTRA.canFly(stack, livingEntity, doTick);
  }

  default boolean hasCapeTexture(ItemStack stack) {
    return true;
  }
}
