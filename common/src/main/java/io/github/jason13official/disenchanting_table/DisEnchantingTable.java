package io.github.jason13official.disenchanting_table;

import io.github.jason13official.disenchanting_table.impl.common.ModConfig;
import io.github.jason13official.disenchanting_table.platform.Services;
import net.minecraft.resources.Identifier;

public class DisEnchantingTable {

  public static void init() {
    ModConfig.load(Services.PLATFORM.getConfigDirectory());
  }

  public static Identifier identifier(final String path) {
    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
  }
}