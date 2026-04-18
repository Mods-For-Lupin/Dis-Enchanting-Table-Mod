package io.github.jason13official.disenchanting_table.impl.client.gui.screens.inventory;

import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class DisEnchantingTableScreen extends AbstractContainerScreen<DisEnchantingTableMenu> {

  public DisEnchantingTableScreen(DisEnchantingTableMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {

  }
}
