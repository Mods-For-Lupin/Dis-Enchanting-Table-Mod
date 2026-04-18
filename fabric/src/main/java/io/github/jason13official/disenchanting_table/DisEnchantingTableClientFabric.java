package io.github.jason13official.disenchanting_table;

import io.github.jason13official.disenchanting_table.impl.client.gui.screens.inventory.DisEnchantingTableScreen;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModMenus;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class DisEnchantingTableClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    DisEnchantingTableClient.init();

    MenuScreens.register(ModMenus.DISENCHANTING_TABLE, DisEnchantingTableScreen::new);
  }
}
