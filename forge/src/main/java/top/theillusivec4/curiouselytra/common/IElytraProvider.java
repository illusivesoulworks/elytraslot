package top.theillusivec4.curiouselytra.common;

import java.awt.Color;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IElytraProvider {

  ResourceLocation TEXTURE = new ResourceLocation("textures/entity/elytra.png");
  Color COLOR = new Color(1.0F, 1.0F, 1.0F, 1.0F);

  default boolean isEnchanted(ItemStack stack) {
    return stack.isEnchanted();
  }

  default ResourceLocation getTexture(ItemStack stack) {
    return TEXTURE;
  }

  default boolean attachCapability(ItemStack stack) {
    return false;
  }

  default Color getColor(ItemStack stack) {
    return COLOR;
  }

  default boolean canFly(ItemStack stack, LivingEntity livingEntity) {
    return stack.canElytraFly(livingEntity);
  }

  boolean matches(ItemStack stack);
}
