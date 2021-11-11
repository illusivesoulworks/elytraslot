package top.theillusivec4.curiouselytra.integration;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.caelus.api.RenderElytraEvent;

public class SpectralElytra implements ICustomElytra {

  private static final ResourceLocation SPECTRAL_ELYTRA =
      new ResourceLocation("mana-and-artifice:spectral_elytra");

  @Override
  public boolean renderElytra(ItemStack stack, RenderElytraEvent evt) {

    if (matches(stack, SPECTRAL_ELYTRA)) {
      evt.setResourceLocation(new ResourceLocation("mana-and-artifice:textures/entity/elytra.png"));
      evt.setEnchanted(true);
      return true;
    }
    return false;
  }
}
