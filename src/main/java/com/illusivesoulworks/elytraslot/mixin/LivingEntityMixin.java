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

package com.illusivesoulworks.elytraslot.mixin;

import com.illusivesoulworks.elytraslot.ElytraSlotNeoForgeMod;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

  @ModifyReturnValue(method = "canGlide", at = @At("RETURN"))
  private boolean elytraslot$allowCurioGliding(boolean vanilla) {
    if (vanilla || !((Object) this instanceof Player player)) {
      return vanilla;
    }
    return !player.onGround() && !player.isPassenger() && !player.hasEffect(MobEffects.LEVITATION)
        && CuriosApi.getCuriosInventory(player)
        .map(inventory -> inventory.isEquipped(ElytraSlotNeoForgeMod::canGlideUsing)).orElse(false);
  }

  @Inject(method = "updateFallFlying", at = @At(value = "INVOKE", target =
      "Lnet/minecraft/util/Util;getRandom(Ljava/util/List;Lnet/minecraft/util/RandomSource;)Ljava/lang/Object;"),
      cancellable = true)
  private void elytraslot$damageCurio(CallbackInfo callback,
                                    @Local List<EquipmentSlot> vanillaGliders) {
    if (!((Object) this instanceof Player player) || !(player.level() instanceof ServerLevel level)) {
      return;
    }
    List<SlotResult> curios = ElytraSlotNeoForgeMod.findGliders(player);
    if (curios.isEmpty()) {
      return;
    }

    // Keep a uniform choice across Curios and vanilla equipment.
    int slot = player.getRandom().nextInt(vanillaGliders.size() + curios.size());
    if (slot >= vanillaGliders.size()) {
      SlotResult curio = curios.get(slot - vanillaGliders.size());
      curio.stack().hurtAndBreak(1, level, player,
          item -> CuriosApi.broadcastCurioBreakEvent(curio.slotContext()));
      player.gameEvent(GameEvent.ELYTRA_GLIDE);
      callback.cancel();
    }
  }
}
