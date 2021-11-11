package top.theillusivec4.curiouselytra.integration;

import java.awt.Color;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gear.SilentGear;
import net.silentchaos512.gear.api.part.PartType;
import net.silentchaos512.gear.client.util.GearClientHelper;
import net.silentchaos512.gear.item.gear.CoreElytra;
import top.theillusivec4.caelus.api.RenderElytraEvent;

public class SilentGearElytra implements ICustomElytra {

  @Override
  public boolean renderElytra(ItemStack stack, RenderElytraEvent evt) {

    if (stack.getItem() instanceof CoreElytra) {
      evt.setResourceLocation(SilentGear.getId("textures/entity/elytra.png"));
      evt.setColor(new Color(GearClientHelper.getColor(stack, PartType.MAIN)));
    }
    return false;
  }
}
