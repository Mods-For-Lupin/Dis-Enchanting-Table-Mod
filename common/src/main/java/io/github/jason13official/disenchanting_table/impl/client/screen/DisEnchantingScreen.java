package io.github.jason13official.disenchanting_table.impl.client.screen;

import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class DisEnchantingScreen extends AbstractContainerScreen<DisEnchantingMenu> {

  public static final Identifier BACKGROUND = DisEnchantingTable.identifier("textures/gui/container/disenchanting_table.png");

  public DisEnchantingScreen(DisEnchantingMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
    super.extractBackground(graphics, mouseX, mouseY, a);
    int xo = (this.width - this.imageWidth) / 2;
    int yo = (this.height - this.imageHeight) / 2;
    graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
  }
}
