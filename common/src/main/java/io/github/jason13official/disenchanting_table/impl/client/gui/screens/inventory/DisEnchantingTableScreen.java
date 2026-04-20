package io.github.jason13official.disenchanting_table.impl.client.gui.screens.inventory;

import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisEnchantingTableTile;
import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingTableMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DisEnchantingTableScreen extends AbstractContainerScreen<DisEnchantingTableMenu> {

  public static final ResourceLocation BACKGROUND = DisEnchantingTable.identifier("textures/gui/container/disenchanting_table.png");

  private Button modeButton;

  public DisEnchantingTableScreen(DisEnchantingTableMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
  }

  @Override
  protected void init() {
    super.init();

    // move labels off-screen.
    this.titleLabelY = -100;
    this.inventoryLabelY = -100;

    this.modeButton = this.addRenderableWidget(
        Button.builder(getModeComponent(), btn ->
            Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.menu.containerId, 0)
        ).bounds(this.leftPos + 7, this.topPos + 7, 60, 16).build()
    );
  }

  @Override
  public void containerTick() {
    super.containerTick();
    this.modeButton.setMessage(getModeComponent());
  }

  private Component getModeComponent() {
    return this.menu.getMode() == DisEnchantingTableTile.MODE_MANUAL
        ? Component.translatable("gui.disenchanting_table.mode.manual")
        : Component.translatable("gui.disenchanting_table.mode.auto");
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    this.renderTooltip(guiGraphics, mouseX, mouseY);
  }
}
