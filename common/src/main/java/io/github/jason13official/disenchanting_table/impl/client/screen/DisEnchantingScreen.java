package io.github.jason13official.disenchanting_table.impl.client.screen;

import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import io.github.jason13official.disenchanting_table.DisEnchantingTableClient;
import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisenchantMode;
import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class DisEnchantingScreen extends AbstractContainerScreen<DisEnchantingMenu> {

  public static final Identifier BACKGROUND =
      DisEnchantingTable.identifier("textures/gui/container/disenchanting_table.png");

  // Progress bar source UV in the texture: 176x0, 22x16 pixels
  private static final int PROGRESS_BAR_U = 176;
  private static final int PROGRESS_BAR_V = 0;
  private static final int PROGRESS_BAR_W = 22;
  private static final int PROGRESS_BAR_H = 16;

  private Button modeButton;

  public DisEnchantingScreen(DisEnchantingMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
  }

  @Override
  protected void init() {
    super.init();
    int xo = (this.width - this.imageWidth) / 2;
    int yo = (this.height - this.imageHeight) / 2;
    this.modeButton = this.addRenderableWidget(Button.builder(
        modeLabel(),
        b -> {
          DisEnchantingTableClient.sendModePacket(this.menu.getPos());
          b.setMessage(modeLabel());
        }
    ).bounds(xo + 7, yo + 56, 60, 12).build());
  }

  @Override
  protected void containerTick() {
    super.containerTick();
    if (this.modeButton != null) {
      this.modeButton.setMessage(modeLabel());
    }
  }

  private Component modeLabel() {
    DisenchantMode mode = this.menu.getMode();
    return mode == DisenchantMode.AUTO
        ? Component.translatable("gui.disenchanting_table.mode.auto")
        : Component.translatable("gui.disenchanting_table.mode.manual");
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
    super.extractBackground(graphics, mouseX, mouseY, a);
    int xo = (this.width - this.imageWidth) / 2;
    int yo = (this.height - this.imageHeight) / 2;
    graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

    int progress = this.menu.getProgress();
    int maxProgress = this.menu.getMaxProgress();
    if (maxProgress > 0 && progress > 0) {
      int barWidth = PROGRESS_BAR_W * progress / maxProgress;
      if (barWidth > 0) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND,
            xo + 79, yo + 34,
            PROGRESS_BAR_U, PROGRESS_BAR_V,
            barWidth, PROGRESS_BAR_H,
            256, 256);
      }
    }
  }
}
