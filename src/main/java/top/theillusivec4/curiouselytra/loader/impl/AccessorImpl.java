package top.theillusivec4.curiouselytra.loader.impl;

import net.minecraft.entity.LivingEntity;
import top.theillusivec4.curiouselytra.core.Accessor;
import top.theillusivec4.curiouselytra.loader.mixin.LivingEntityAccessor;

public class AccessorImpl implements Accessor {

  @Override
  public int getFlyingTicks(LivingEntity livingEntity) {
    return ((LivingEntityAccessor) livingEntity).getRoll();
  }
}
