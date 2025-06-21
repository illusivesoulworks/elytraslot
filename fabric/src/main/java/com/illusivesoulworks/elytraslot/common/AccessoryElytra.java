package com.illusivesoulworks.elytraslot.common;

import io.wispforest.accessories.api.Accessory;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class AccessoryElytra implements Accessory {

  @Override
  public boolean canEquip(ItemStack stack, SlotReference reference) {
    return stack.getItem() != reference.entity().getItemBySlot(EquipmentSlot.CHEST).getItem();
  }
}
