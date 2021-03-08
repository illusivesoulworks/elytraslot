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

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
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
import top.theillusivec4.curiouselytra.integration.NetheritePlusIntegration;
import top.theillusivec4.curiouselytra.integration.SilentGearIntegration;

@Mod(CuriousElytra.MODID)
public class CuriousElytra {

  public static final String MODID = "curiouselytra";

  public static boolean isNetheritePlusLoaded = false;
  public static boolean isSilentGearLoaded = false;

  public CuriousElytra() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::enqueue);
    eventBus.addListener(this::clientSetup);
    eventBus.addListener(this::setup);
    isNetheritePlusLoaded = ModList.get().isLoaded("netherite_plus");
    isSilentGearLoaded = ModList.get().isLoaded("silentgear");
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
            .findEquippedCurio((stack) -> CaelusApi.canElytraFly(player, stack), player)
            .ifPresent(triple -> attributeInstance
                .applyNonPersistentModifier(CurioElytra.ELYTRA_CURIO_MODIFIER));
      }
    }
  }

  private void attachCapabilities(final AttachCapabilitiesEvent<ItemStack> evt) {
    ItemStack stack = evt.getObject();

    if (stack.getItem() instanceof ElytraItem ||
        (isNetheritePlusLoaded && NetheritePlusIntegration.isNetheriteElytra(stack.getItem()))) {
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

            if (CaelusApi.isElytra(stack) && stacksHandler.getRenders().get(i)) {
              evt.setRender(true);
              ResourceLocation rl = stack.getItem().getRegistryName();

              if (rl != null) {

                if (rl.equals(new ResourceLocation("enderitemod:enderite_elytra_seperated"))) {
                  evt.setResourceLocation(
                      new ResourceLocation("minecraft:textures/entity/enderite_elytra.png"));
                } else if (rl.equals(new ResourceLocation("netherite_plus:netherite_elytra"))) {
                  evt.setResourceLocation(
                      new ResourceLocation("netherite_plus:textures/entity/netherite_elytra.png"));
                } else if (isSilentGearLoaded && SilentGearIntegration.isSilentGearElytra(stack)) {
                  evt.setResourceLocation(SilentGearIntegration.getElytraTexture());
                  evt.setColor(SilentGearIntegration.getElytraColor(stack));
                }
              }

              if (stack.isEnchanted()) {
                evt.setEnchanted(true);
              }
            }
          }
        });
      }
    });
  }
}
