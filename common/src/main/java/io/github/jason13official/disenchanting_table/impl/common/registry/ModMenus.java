package io.github.jason13official.disenchanting_table.impl.common.registry;

import io.github.jason13official.disenchanting_table.Constants;
import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingTableMenu;
import io.github.jason13official.disenchanting_table.platform.Services;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {

  public static MenuType<DisEnchantingTableMenu> DISENCHANTING_TABLE;

  public static void register(BiConsumer<MenuType<?>, ResourceLocation> consumer) {

    DISENCHANTING_TABLE = Services.registry().createMenu(DisEnchantingTableMenu::new);

    consumer.accept(DISENCHANTING_TABLE, DisEnchantingTable.identifier(Constants.MOD_ID));
  }
}
