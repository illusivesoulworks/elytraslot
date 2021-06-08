/*
 * Copyright (c) 2019-2021 C4
 *
 * This file is part of Elytra Trinket, a mod made for Minecraft.
 *
 * Elytra Trinket is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Elytra Trinket is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Elytra Trinket.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.elytratrinket;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import java.util.UUID;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.event.GameEvent;
import top.theillusivec4.caelus.api.CaelusApi;
import top.theillusivec4.elytratrinket.mixin.AccessorLivingEntity;

public class ElytraTrinket implements Trinket {

  private static final EntityAttributeModifier ELYTRA_TRINKET_MODIFIER =
      new EntityAttributeModifier(
          UUID.fromString("c754faef-9926-4a77-abbe-e34ef0d735aa"), "Elytra trinket modifier", 1.0D,
          EntityAttributeModifier.Operation.ADDITION);

  @Override
  public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
    int i = ((AccessorLivingEntity) entity).getRoll() + 1;

    if (!entity.world.isClient && i % 10 == 0) {
      int j = i / 10;

      if (j % 2 == 0) {
        stack.damage(1, entity, e1 -> TrinketsApi.onTrinketBroken(stack, slot, e1));
      }
      entity.emitGameEvent(GameEvent.ELYTRA_FREE_FALL);
    }
    EntityAttributeInstance att =
        entity.getAttributeInstance(CaelusApi.getInstance().getFlightAttribute());

    if (ElytraItem.isUsable(stack)) {

      if (att != null && !att.hasModifier(ELYTRA_TRINKET_MODIFIER)) {
        att.addTemporaryModifier(ELYTRA_TRINKET_MODIFIER);
      }
    } else {
      att.removeModifier(ELYTRA_TRINKET_MODIFIER);
    }
  }

  @Override
  public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
    EntityAttributeInstance att =
        entity.getAttributeInstance(CaelusApi.getInstance().getFlightAttribute());

    if (att != null) {
      att.removeModifier(ELYTRA_TRINKET_MODIFIER);
    }
  }
}
