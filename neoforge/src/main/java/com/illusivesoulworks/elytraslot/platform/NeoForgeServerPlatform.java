package com.illusivesoulworks.elytraslot.platform;

import com.illusivesoulworks.elytraslot.platform.services.IServerPlatform;
import net.neoforged.fml.ModList;

public class NeoForgeServerPlatform implements IServerPlatform {

  @Override
  public boolean isModLoaded(String modId) {
    return MODS.computeIfAbsent(modId, k -> ModList.get().isLoaded(modId));
  }
}
