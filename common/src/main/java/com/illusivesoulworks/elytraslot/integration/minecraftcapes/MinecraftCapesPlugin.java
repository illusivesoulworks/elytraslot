package com.illusivesoulworks.elytraslot.integration.minecraftcapes;

import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftcapes.ExtendedPlayerRenderState;
import net.minecraftcapes.config.MinecraftCapesConfig;
import net.minecraftcapes.player.PlayerHandler;

public class MinecraftCapesPlugin {

  public static ResourceLocation getCapeLocation(PlayerRenderState playerRenderState) {

    if (playerRenderState instanceof ExtendedPlayerRenderState extState) {
      PlayerHandler playerHandler = extState.getMinecraftCapes$playerHandler();

      if (playerHandler.getCapeLocation() != null && MinecraftCapesConfig.isCapeVisible()) {
        return playerHandler.getCapeLocation();
      }
    }
    return null;
  }
}
