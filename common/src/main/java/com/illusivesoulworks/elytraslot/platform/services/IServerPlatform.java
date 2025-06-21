package com.illusivesoulworks.elytraslot.platform.services;

import java.util.HashMap;
import java.util.Map;

public interface IServerPlatform {

  Map<String, Boolean> MODS = new HashMap<>();

  boolean isModLoaded(String modId);
}
