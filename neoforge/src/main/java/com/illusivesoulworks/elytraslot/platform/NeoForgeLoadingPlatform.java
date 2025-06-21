package com.illusivesoulworks.elytraslot.platform;

import com.illusivesoulworks.elytraslot.platform.services.ILoadingPlatform;
import com.illusivesoulworks.elytraslot.platform.services.IServerPlatform;
import net.neoforged.fml.loading.FMLLoader;

public class NeoForgeLoadingPlatform implements ILoadingPlatform {

  @Override
  public boolean isModLoaded(String modId) {
    return FMLLoader.getLoadingModList().getModFileById(modId) != null;
  }
}
