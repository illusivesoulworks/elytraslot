package top.theillusivec4.elytratrinket.client;

import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

public class ElytraTrinketClientMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register(this::lacksElytraTrinket);
    LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
        (entityType, entityRenderer, registrationHelper, context) -> registrationHelper.register(
            new ElytraTrinketRenderer<>(entityRenderer, context.getModelLoader())));
  }

  private boolean lacksElytraTrinket(PlayerEntity player) {
    return TrinketsApi.getTrinketComponent(player)
        .map(trinketComponent -> !trinketComponent.isEquipped(Items.ELYTRA)).orElse(true);
  }
}
