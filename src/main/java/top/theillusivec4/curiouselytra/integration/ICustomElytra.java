package top.theillusivec4.curiouselytra.integration;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.caelus.api.RenderElytraEvent;

public interface ICustomElytra {

  boolean renderElytra(ItemStack stack, RenderElytraEvent evt);

  default boolean attachCapability(ItemStack stack) {
    return false;
  }

  default boolean matches(ItemStack stack, ResourceLocation resourceLocation) {
    ResourceLocation rl = stack.getItem().getRegistryName();

    if (rl != null) {
      return rl.equals(resourceLocation);
    }
    return false;
  }
}
