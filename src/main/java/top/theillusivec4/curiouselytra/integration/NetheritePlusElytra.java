package top.theillusivec4.curiouselytra.integration;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.caelus.api.RenderElytraEvent;

public class NetheritePlusElytra implements ICustomElytra {

  private static final ResourceLocation NETHERITE_ELYTRA =
      new ResourceLocation("netherite_plus:netherite_elytra");

  @Override
  public boolean attachCapability(ItemStack stack) {
    return matches(stack, NETHERITE_ELYTRA);
  }

  @Override
  public boolean renderElytra(ItemStack stack, RenderElytraEvent evt) {

    if (matches(stack, NETHERITE_ELYTRA)) {
      evt.setResourceLocation(
          new ResourceLocation("netherite_plus:textures/entity/netherite_elytra.png"));
      return true;
    }
    return false;
  }
}
