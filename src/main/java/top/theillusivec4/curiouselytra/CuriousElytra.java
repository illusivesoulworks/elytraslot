/*
 * Copyright (C) 2019  C4
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

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.ItemHandlerHelper;
import top.theillusivec4.caelus.api.event.RenderCapeCheckEvent;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.CuriosRegistry;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod(CuriousElytra.MODID)
public class CuriousElytra {

    public static final String MODID = "curiouselytra";

    public CuriousElytra() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setup(final FMLCommonSetupEvent evt) {
        CuriosRegistry.getOrRegisterType("back").icon(new ResourceLocation(MODID, "item/empty_back_slot"));
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
        ItemStack stack = evt.getObject();

        if (stack.getItem() == Items.ELYTRA) {
            CurioElytra curioElytra = new CurioElytra(stack);
            evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
                LazyOptional<ICurio> curio = LazyOptional.of(() -> curioElytra);

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
                    return CuriosCapability.ITEM.orEmpty(cap, curio);
                }
            });
        }
    }

    @SubscribeEvent
    public void onLivingEquipmentChange(LivingEquipmentChangeEvent evt) {
        ItemStack to = evt.getTo();

        if (evt.getSlot() == EntityEquipmentSlot.CHEST && to.getItem() == Items.ELYTRA) {
            EntityLivingBase livingBase = evt.getEntityLiving();
            CuriosAPI.FinderData data = CuriosAPI.getCurioEquipped(Items.ELYTRA, livingBase);

            if (data != null) {
                ItemStack stack = data.getStack();
                ItemStack copy = stack.copy();
                CuriosAPI.getCuriosHandler(livingBase).ifPresent(handler -> {
                    handler.setStackInSlot(data.getIdentifier(), data.getIndex(), ItemStack.EMPTY);
                });

                if (livingBase instanceof EntityPlayer) {
                    ItemHandlerHelper.giveItemToPlayer((EntityPlayer) livingBase, copy);
                } else {
                    livingBase.entityDropItem(copy);
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onRenderCapeCheck(RenderCapeCheckEvent evt) {

            if (CuriosAPI.getCurioEquipped(Items.ELYTRA, evt.getEntityLiving()) != null) {
                evt.setResult(Event.Result.DENY);
            }
        }
    }
}
