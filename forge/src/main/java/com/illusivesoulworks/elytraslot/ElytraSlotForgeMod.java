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

import com.illusivesoulworks.elytraslot.common.CurioElytra;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.caelus.api.CaelusApi;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICurio;

@Mod(ElytraSlotConstants.MOD_ID)
public class ElytraSlotForgeMod {

  public ElytraSlotForgeMod() {
    ElytraSlotCommonMod.init();
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::clientSetup);
    eventBus.addListener(this::setup);
  }

  private void setup(final FMLCommonSetupEvent evt) {
    MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, this::attachCapabilities);
    MinecraftForge.EVENT_BUS.addListener(this::playerTick);
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    ElytraSlotForgeClientMod.setup();
  }

  private void playerTick(final TickEvent.PlayerTickEvent evt) {
    Player player = evt.player;
    AttributeInstance attributeInstance =
        player.getAttribute(CaelusApi.getInstance().getFlightAttribute());

    if (attributeInstance != null) {
      attributeInstance.removeModifier(CurioElytra.ELYTRA_CURIO_MODIFIER);

      if (!attributeInstance.hasModifier(CurioElytra.ELYTRA_CURIO_MODIFIER) &&
          ElytraSlotCommonMod.canFly(player, false)) {
        attributeInstance.addTransientModifier(CurioElytra.ELYTRA_CURIO_MODIFIER);
      }
    }
  }

  private void attachCapabilities(final AttachCapabilitiesEvent<ItemStack> evt) {
    ItemStack stack = evt.getObject();

    if (ElytraSlotCommonMod.IS_ELYTRA.test(stack)) {
      final LazyOptional<ICurio> elytraCurio = LazyOptional.of(() -> new CurioElytra(stack));
      evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
                                                 @Nullable Direction side) {
          return CuriosCapability.ITEM.orEmpty(cap, elytraCurio);
        }
      });
      evt.addListener(elytraCurio::invalidate);
    }
  }
}