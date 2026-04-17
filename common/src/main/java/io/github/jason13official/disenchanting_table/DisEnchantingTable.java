package io.github.jason13official.disenchanting_table;

import net.minecraft.resources.ResourceLocation;


public class DisEnchantingTable {

  public static void init() {
  }

  public static ResourceLocation identifier(final String path) {
    return new ResourceLocation(Constants.MOD_ID, path);
  }
}