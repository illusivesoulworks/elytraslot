package com.illusivesoulworks.elytraslot.mixin.integration.waveycapes;

import com.illusivesoulworks.elytraslot.platform.ClientServices;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "dev.tr7zw.waveycapes.renderlayers.CustomCapeRenderLayer", remap = false)
public class CustomCapeRenderLayerMixin {

  @Inject(at = @At("HEAD"), method = "render", cancellable = true)
  private void elytraslot$render(PoseStack poseStack, MultiBufferSource multiBufferSource,
                                 int packedLight, PlayerRenderState renderState, float yRot,
                                 float xRot, CallbackInfo ci) {

    if (!ClientServices.CLIENT.getRenderingElytra(renderState).isEmpty()) {
      ci.cancel();
    }
  }
}
