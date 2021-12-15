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
import java.util.Set;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
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
import top.theillusivec4.caelus.api.RenderElytraEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curiouselytra.integration.AetherCape;
import top.theillusivec4.curiouselytra.integration.EnderiteElytra;
import top.theillusivec4.curiouselytra.integration.ICustomElytra;
import top.theillusivec4.curiouselytra.integration.NetheritePlusElytra;
import top.theillusivec4.curiouselytra.integration.SilentGearElytra;
import top.theillusivec4.curiouselytra.integration.SpectralElytra;

@Mod(CuriousElytra.MOD_ID)
public class CuriousElytra {

  public static final String MOD_ID = "curiouselytra";

  private static final Map<String, Supplier<Supplier<ICustomElytra>>> INTEGRATIONS =
      new HashMap<>();
  private static final List<ICustomElytra> ACTIVE_INTEGRATIONS = new ArrayList<>();

  static {
    INTEGRATIONS.put("aether", () -> AetherCape::new);
    INTEGRATIONS.put("silentgear", () -> SilentGearElytra::new);
    INTEGRATIONS.put("netherite_plus", () -> NetheritePlusElytra::new);
    INTEGRATIONS.put("mana-and-artifice", () -> SpectralElytra::new);
    INTEGRATIONS.put("enderitemod", () -> EnderiteElytra::new);
  }

  public CuriousElytra() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::enqueue);
    eventBus.addListener(this::clientSetup);
    eventBus.addListener(this::setup);
    ModList modList = ModList.get();

    for (Map.Entry<String, Supplier<Supplier<ICustomElytra>>> entry : INTEGRATIONS.entrySet()) {

      if (modList.isLoaded(entry.getKey())) {
        ACTIVE_INTEGRATIONS.add(entry.getValue().get().get());
      }
    }
  }

  private void setup(final FMLCommonSetupEvent evt) {
    MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, this::attachCapabilities);
    MinecraftForge.EVENT_BUS.addListener(this::playerTick);
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    MinecraftForge.EVENT_BUS.addListener(this::renderElytra);
  }

  private void enqueue(final InterModEnqueueEvent evt) {
    InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
        () -> SlotTypePreset.BACK.getMessageBuilder().build());
  }

  private void playerTick(final TickEvent.PlayerTickEvent evt) {
    PlayerEntity player = evt.player;
    ModifiableAttributeInstance attributeInstance =
        player.getAttribute(CaelusApi.ELYTRA_FLIGHT.get());

    if (attributeInstance != null) {
      attributeInstance.removeModifier(CurioElytra.ELYTRA_CURIO_MODIFIER);

      if (!attributeInstance.hasModifier(CurioElytra.ELYTRA_CURIO_MODIFIER)) {
        CuriosApi.getCuriosHelper()
            .findEquippedCurio((stack) -> CaelusApi.canElytraFly(player, stack), player).ifPresent(
                triple -> attributeInstance.applyNonPersistentModifier(
                    CurioElytra.ELYTRA_CURIO_MODIFIER));
      }
    }
  }

  private void attachCapabilities(final AttachCapabilitiesEvent<ItemStack> evt) {
    ItemStack stack = evt.getObject();
    boolean attachable = stack.getItem() instanceof ElytraItem;

    if (!attachable) {

      for (ICustomElytra module : ACTIVE_INTEGRATIONS) {

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

  private void renderElytra(final RenderElytraEvent evt) {
    PlayerEntity playerEntity = evt.getPlayer();
    CuriosApi.getCuriosHelper().getCuriosHandler(playerEntity).ifPresent(handler -> {
      Set<String> tags = CuriosApi.getCuriosHelper().getCurioTags(Items.ELYTRA);

      for (String id : tags) {
        handler.getStacksHandler(id).ifPresent(stacksHandler -> {
          IDynamicStackHandler stackHandler = stacksHandler.getStacks();

          for (int i = 0; i < stackHandler.getSlots(); i++) {
            ItemStack stack = stackHandler.getStackInSlot(i);

            if (stacksHandler.getRenders().get(i)) {
              boolean isElytra = CaelusApi.isElytra(stack);

              for (ICustomElytra module : ACTIVE_INTEGRATIONS) {

                if (module.renderElytra(stack, evt)) {
                  isElytra = true;
                  break;
                }
              }

              if (isElytra) {
                evt.setRender(true);

                if (stack.isEnchanted()) {
                  evt.setEnchanted(true);
                }
                return;
              }
            }
          }
        });
      }
    });
  }
}
