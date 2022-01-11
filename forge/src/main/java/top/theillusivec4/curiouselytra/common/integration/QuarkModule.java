package top.theillusivec4.curiouselytra.common.integration;

import net.minecraft.world.item.ItemStack;
import vazkii.quark.content.tools.module.ColorRunesModule;

public class QuarkModule {

  public static void setColoredStack(ItemStack stack) {
    ColorRunesModule.setTargetStack(stack);
  }
}
