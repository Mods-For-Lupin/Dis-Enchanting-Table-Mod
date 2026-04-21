package io.github.jason13official.disenchanting_table;

import net.minecraft.resources.Identifier;

public class DisEnchantingTable {

  public static void init() {
  }

  public static Identifier identifier(final String path) {
    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
  }
}