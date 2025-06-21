package com.illusivesoulworks.elytraslot.platform;

import com.illusivesoulworks.elytraslot.platform.services.IServerPlatform;
import net.fabricmc.loader.api.FabricLoader;

public class FabricServerPlatform implements IServerPlatform {

  @Override
  public boolean isModLoaded(String modId) {
    return MODS.computeIfAbsent(modId, k -> FabricLoader.getInstance().isModLoaded(k));
  }
}
