package top.theillusivec4.curiouselytra.core;

import top.theillusivec4.curiouselytra.loader.impl.AccessorImpl;

public class CuriousElytra {

  private static final Accessor ACCESSOR = new AccessorImpl();

  public static Accessor getAccessor() {
    return ACCESSOR;
  }
}
