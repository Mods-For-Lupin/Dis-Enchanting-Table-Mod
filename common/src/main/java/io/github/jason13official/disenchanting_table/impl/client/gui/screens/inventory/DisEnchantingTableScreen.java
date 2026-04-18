package io.github.jason13official.disenchanting_table.impl.client.gui.screens.inventory;

import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingTableMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DisEnchantingTableScreen extends AbstractContainerScreen<DisEnchantingTableMenu> {

  public static final ResourceLocation BACKGROUND = DisEnchantingTable.identifier("textures/gui/container/disenchanting_table.png");

  public DisEnchantingTableScreen(DisEnchantingTableMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
  }

  @Override
  protected void init() {
    super.init();

    // move labels off-screen.
    this.titleLabelY = -100;
    this.inventoryLabelY = -100;
  }

  /// maybe not needed? original resize clears and rebuilds widgets as a side effect
  @Override
  public void resize(Minecraft minecraft, int width, int height) {
    init();
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
  }
}
