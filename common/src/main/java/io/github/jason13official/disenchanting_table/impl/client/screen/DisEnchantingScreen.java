package io.github.jason13official.disenchanting_table.impl.client.screen;

import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import io.github.jason13official.disenchanting_table.DisEnchantingTableClient;
import io.github.jason13official.disenchanting_table.impl.client.ClientConfig;
import io.github.jason13official.disenchanting_table.impl.common.ModConfig;
import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisEnchantingTableTile;
import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisenchantMode;
import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingMenu;
import io.github.jason13official.disenchanting_table.impl.common.util.ExperienceHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DisEnchantingScreen extends AbstractContainerScreen<DisEnchantingMenu> {

  public static final Identifier BACKGROUND = DisEnchantingTable.identifier("textures/gui/container/disenchanting_table.png");

  private static final Supplier<ItemStack> ENCHANTED_HOPPER = () -> {
    ItemStack stack = new ItemStack(Items.HOPPER);
    stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
    return stack;
  };

  // Progress bar source UV in the texture: 176x0, 22x16 pixels
  private static final int PROGRESS_BAR_U = 176;
  private static final int PROGRESS_BAR_V = 21;
  private static final int PROGRESS_BAR_W = 22;
  private static final int PROGRESS_BAR_H = 16;

  // private Button modeButton;

  public DisEnchantingScreen(DisEnchantingMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
  }

  @Override
  protected void init() {
    super.init();

    // moves labels off-screen to reduce clutter
    this.titleLabelY = 9999;
    this.inventoryLabelY = 9999;

    int xo = (this.width - this.imageWidth) / 2;
    int yo = (this.height - this.imageHeight) / 2;
//    this.modeButton = this.addRenderableWidget(Button.builder(
//        modeLabel(),
//        b -> {
//          // Constants.LOG.info("clicked toggle");
//          DisEnchantingTableClient.sendModePacket();
//          b.setMessage(modeLabel());
//        }
//    // ).bounds(xo + 7, yo + 56, 60, 12).build());
//    // ).bounds(xo + 7, yo + 68, 60, 12).build());
//    ).bounds(xo + 0, yo - 12, 70, 12).build());
  }

  @Override
  public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {

    int xo = (this.width - this.imageWidth) / 2;
    int yo = (this.height - this.imageHeight) / 2;

    int button = event.button();
    double x = event.x();
    double y = event.y();

    double lowerX = xo + 86;
    double lowerY = yo + 7;
    if (button == 0 && (x >= lowerX && x < lowerX + 16) && (y >= lowerY && y < lowerY + 16)) {
      DisEnchantingTableClient.sendModePacketToServer();
    }

    return super.mouseClicked(event, doubleClick);
  }

  @Override
  protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
    super.extractTooltip(graphics, mouseX, mouseY);

    int xo = (this.width - this.imageWidth) / 2;
    int yo = (this.height - this.imageHeight) / 2;
    double lowerX = xo + 86;
    double lowerY = yo + 7;
    if (isHovering(mouseX, mouseY, (int) lowerX, (int) lowerY, 16, 16)) {
      // graphics.setTooltipForNextFrame(this.font, Component.literal("Tooltip!"), mouseX, mouseY);

      List<FormattedCharSequence> lines = new ArrayList<>();
      lines.add(FormattedCharSequence.forward("Mode: " + this.menu.getMode().toString(), Style.EMPTY.withBold(true)));
      if (this.menu.getMode() == DisenchantMode.AUTO) {
        lines.add(FormattedCharSequence.forward(" Experience is paid from nearest player,", Style.EMPTY.withBold(false)));
        lines.add(FormattedCharSequence.forward(" hopper interactions are ENABLED.", Style.EMPTY.withBold(false)));
      } else {
        lines.add(FormattedCharSequence.forward(" Experience is paid when taking output,", Style.EMPTY.withBold(false)));
        lines.add(FormattedCharSequence.forward(" hopper interactions are DISABLED.", Style.EMPTY.withBold(false)));
      }
      graphics.setTooltipForNextFrame(this.font, lines, mouseX, mouseY);
    }
  }

  private boolean isHovering(int mouseX, int mouseY, int x, int y, int width, int height) {
    return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
  }

  @Override
  protected void containerTick() {
    super.containerTick();
//    if (this.modeButton != null) {
//      this.modeButton.setMessage(modeLabel());
//      boolean outputOccupied = this.menu.getMode() == DisenchantMode.AUTO
//          && !this.menu.getContainer().getItem(2).isEmpty();
//      this.modeButton.active = !outputOccupied;
//    }
  }

  private Component modeLabel() {
    DisenchantMode mode = this.menu.getMode();
    return mode == DisenchantMode.AUTO ? Component.translatable("gui.disenchanting_table.mode.auto") : Component.translatable("gui.disenchanting_table.mode.manual");
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
    super.extractBackground(graphics, mouseX, mouseY, a);
    int xo = (this.width - this.imageWidth) / 2;
    int yo = (this.height - this.imageHeight) / 2;
    graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

    if (this.menu.getMode() == DisenchantMode.AUTO) {
      // graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, xo + 86, yo + 7, 176, 48, 16, 16, 256, 256);
      // graphics.fakeItem(ENCHANTED_HOPPER.get(), xo + 86, yo + 7);
      graphics.fakeItem(ENCHANTED_HOPPER.get(), xo + 86, yo + 7);
    } else {
      graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, xo + 86, yo + 7, 176, 64, 16, 16, 256, 256);
    }

    int progress = this.menu.getProgress();
    int maxProgress = this.menu.getMaxProgress();
    if (maxProgress > 0 && progress > 0) {
      int barWidth = PROGRESS_BAR_W * progress / maxProgress;
      if (barWidth > 0) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND,
            // xo + 79, yo + 34,
            xo + 102, yo + 45, PROGRESS_BAR_U, PROGRESS_BAR_V, barWidth, PROGRESS_BAR_H, 256, 256);
      }
    }

  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
    super.extractLabels(graphics, xm, ym);
    if (!ClientConfig.get().renderExperienceCost || !ModConfig.get().requiresExperience) {
      return;
    }
    if (this.menu.getContainer().getItem(0).isEmpty()) {
      return;
    }

    int cost = DisEnchantingTableTile.computeXpCost();
    if (cost <= 0) {
      return;
    }

    Player player = this.minecraft.player;
    boolean hasEnough = ModConfig.get().usesPoints ? ExperienceHelper.hasEnoughExperiencePoints(player, cost) : ExperienceHelper.hasEnoughExperienceLevels(player, cost);

    Component line = ModConfig.get().usesPoints ? Component.translatable("container.disenchanting_table.cost_points", cost) : Component.translatable("container.disenchanting_table.cost_levels", cost);

    int color = hasEnough ? -8323296 : -40864;

    // int center = this.leftPos + this.imageWidth / 2;
    int center = this.imageWidth / 2;
    int textWidth = this.font.width(line);
    int textStartX = center - textWidth / 2;
    graphics.fill(textStartX - 2, 69 - 2, textStartX + textWidth + 2, 69 + 9 + 2, 1325400064);
    graphics.text(this.font, line, textStartX, 69, color); // nice

    // int tx = this.imageWidth - 8 - this.font.width(line) - 2;
    // int ty = 69;
    // graphics.fill(tx - 2, ty - 2, this.imageWidth - 8, ty + 9, 1325400064);
    // graphics.text(this.font, line, tx, ty, color);
  }
}
