package io.github.jason13official.disenchanting_table.impl.client.screen;

import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class DisEnchantingScreen extends AbstractContainerScreen<DisEnchantingMenu> {

  public DisEnchantingScreen(DisEnchantingMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
  }
}
