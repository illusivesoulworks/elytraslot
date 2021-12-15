package top.theillusivec4.curiouselytra.common.integration;

import java.awt.Color;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.gear.SilentGear;
import net.silentchaos512.gear.api.part.PartType;
import net.silentchaos512.gear.client.util.GearClientHelper;
import net.silentchaos512.gear.item.gear.GearElytraItem;
import top.theillusivec4.curiouselytra.common.IElytraProvider;

public class SilentGearElytraProvider implements IElytraProvider {

  @Override
  public ResourceLocation getTexture(ItemStack stack) {
    return SilentGear.getId("textures/entity/elytra.png");
  }

  @Override
  public boolean matches(ItemStack stack) {
    return stack.getItem() instanceof GearElytraItem;
  }

  @Override
  public Color getColor(ItemStack stack) {
    return new Color(GearClientHelper.getColor(stack, PartType.MAIN));
  }
}
