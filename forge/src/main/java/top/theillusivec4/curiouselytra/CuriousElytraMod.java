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

package top.theillusivec4.curiouselytra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
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
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curiouselytra.client.CuriousElytraClientMod;
import top.theillusivec4.curiouselytra.common.CurioElytra;
import top.theillusivec4.curiouselytra.common.IElytraProvider;
import top.theillusivec4.curiouselytra.common.VanillaElytraProvider;
import top.theillusivec4.curiouselytra.common.integration.SilentGearElytraProvider;

@Mod(CuriousElytraMod.MOD_ID)
public class CuriousElytraMod {

  public static final String MOD_ID = "curiouselytra";

  private static final Map<String, Supplier<Supplier<IElytraProvider>>> PROVIDERS =
      new HashMap<>();
  private static final List<IElytraProvider> ACTIVE_PROVIDERS = new ArrayList<>();

  static {
    PROVIDERS.put("silentgear", () -> SilentGearElytraProvider::new);
  }

  public CuriousElytraMod() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::enqueue);
    eventBus.addListener(this::clientSetup);
    eventBus.addListener(this::setup);
    ModList modList = ModList.get();

    for (Map.Entry<String, Supplier<Supplier<IElytraProvider>>> entry : PROVIDERS.entrySet()) {

      if (modList.isLoaded(entry.getKey())) {
        ACTIVE_PROVIDERS.add(entry.getValue().get().get());
      }
    }
    ACTIVE_PROVIDERS.add(new VanillaElytraProvider());
  }

  private void setup(final FMLCommonSetupEvent evt) {
    MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, this::attachCapabilities);
    MinecraftForge.EVENT_BUS.addListener(this::playerTick);
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    CuriousElytraClientMod.setup();
  }

  private void enqueue(final InterModEnqueueEvent evt) {
    InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
        () -> SlotTypePreset.BACK.getMessageBuilder().build());
  }

  private void playerTick(final TickEvent.PlayerTickEvent evt) {
    Player player = evt.player;
    AttributeInstance attributeInstance =
        player.getAttribute(CaelusApi.getInstance().getFlightAttribute());

    if (attributeInstance != null) {
      attributeInstance.removeModifier(CurioElytra.ELYTRA_CURIO_MODIFIER);

      if (!attributeInstance.hasModifier(CurioElytra.ELYTRA_CURIO_MODIFIER) &&
          getElytraProvider(player, true).isPresent()) {
        attributeInstance.addTransientModifier(CurioElytra.ELYTRA_CURIO_MODIFIER);
      }
    }
  }

  private void attachCapabilities(final AttachCapabilitiesEvent<ItemStack> evt) {
    ItemStack stack = evt.getObject();
    boolean attachable = stack.getItem() == Items.ELYTRA;

    if (!attachable) {

      for (IElytraProvider module : ACTIVE_PROVIDERS) {

        if (module.attachCapability(stack)) {
          attachable = true;
          break;
        }
      }
    }

    if (attachable) {
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

  public static Optional<IElytraProvider> getElytraProvider(final LivingEntity livingEntity,
                                                            boolean shouldFly) {
    AtomicReference<IElytraProvider> result = new AtomicReference<>();
    CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity).ifPresent(curios -> {

      for (Map.Entry<String, ICurioStacksHandler> entry : curios.getCurios().entrySet()) {
        IDynamicStackHandler stacks = entry.getValue().getStacks();

        for (int i = 0; i < stacks.getSlots(); i++) {
          ItemStack stack = stacks.getStackInSlot(i);

          for (IElytraProvider provider : ACTIVE_PROVIDERS) {

            if (provider.matches(stack) && (!shouldFly || provider.canFly(stack, livingEntity))) {
              result.set(provider);
            }
          }
        }
      }
    });
    return Optional.ofNullable(result.get());
  }
}
