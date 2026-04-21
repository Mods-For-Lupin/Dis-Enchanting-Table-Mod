package io.github.jason13official.disenchanting_table.impl.common.registry;

import io.github.jason13official.disenchanting_table.Constants;
import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingMenu;
import io.github.jason13official.disenchanting_table.platform.Services;
import java.util.function.BiConsumer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {

  public static MenuType<DisEnchantingMenu> DISENCHANTING_TABLE;

  public static void register(BiConsumer<MenuType<?>, Identifier> consumer) {

    DISENCHANTING_TABLE = Services.registry().menu(DisEnchantingMenu::new, FeatureFlags.VANILLA_SET);
    consumer.accept(DISENCHANTING_TABLE, DisEnchantingTable.identifier(Constants.MOD_ID));
  }
}
