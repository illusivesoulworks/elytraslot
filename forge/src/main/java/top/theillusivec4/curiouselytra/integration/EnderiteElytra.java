package top.theillusivec4.curiouselytra.integration;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.caelus.api.RenderElytraEvent;

public class EnderiteElytra implements ICustomElytra {

  private static final ResourceLocation ENDERITE_ELYTRA =
      new ResourceLocation("enderitemod:enderite_elytra_seperated");

  @Override
  public boolean renderElytra(ItemStack stack, RenderElytraEvent evt) {

    if (matches(stack, ENDERITE_ELYTRA)) {
      evt.setResourceLocation(
          new ResourceLocation("minecraft:textures/entity/enderite_elytra.png"));
      return true;
    }
    return false;
  }
}
