package top.theillusivec4.curiouselytra.integration;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class NetheritePlusIntegration {

  public static boolean isNetheriteElytra(Item item) {
    ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
    return rl != null && isNetheriteElytra(rl);
  }

  public static boolean isNetheriteElytra(ResourceLocation id) {
    return id.equals(new ResourceLocation("netherite_plus:netherite_elytra"));
  }
}
