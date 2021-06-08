package top.theillusivec4.elytratrinket.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback;

public class ElytraTrinketClientMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
      registrationHelper.register(new ElytraTrinketRenderer<>(entityRenderer, context.getModelLoader()));
    });
  }
}
