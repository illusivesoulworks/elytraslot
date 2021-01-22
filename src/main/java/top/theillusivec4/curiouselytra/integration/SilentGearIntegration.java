package top.theillusivec4.curiouselytra.integration;

import java.awt.Color;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gear.SilentGear;
import net.silentchaos512.gear.api.part.PartType;
import net.silentchaos512.gear.client.util.GearClientHelper;
import net.silentchaos512.gear.item.gear.CoreElytra;

public class SilentGearIntegration {

  public static boolean isSilentGearElytra(ItemStack stack) {
    return stack.getItem() instanceof CoreElytra;
  }

  public static ResourceLocation getElytraTexture() {
    return SilentGear.getId("textures/entity/elytra.png");
  }

  public static Color getElytraColor(ItemStack stack) {
    return new Color(GearClientHelper.getColor(stack, PartType.MAIN));
  }
}
