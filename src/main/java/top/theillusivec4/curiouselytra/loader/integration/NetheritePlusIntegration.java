package top.theillusivec4.curiouselytra.loader.integration;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NetheritePlusIntegration {

  public static boolean isNetheriteElytra(Item item) {
    return isNetheriteElytra(Registry.ITEM.getId(item));
  }

  public static boolean isNetheriteElytra(Identifier id) {
    return id.equals(new Identifier("netherite_plus:netherite_elytra"));
  }
}
