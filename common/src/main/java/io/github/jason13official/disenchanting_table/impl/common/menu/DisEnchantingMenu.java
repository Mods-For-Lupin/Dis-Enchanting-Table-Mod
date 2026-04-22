package io.github.jason13official.disenchanting_table.impl.common.menu;

import io.github.jason13official.disenchanting_table.impl.common.ModConfig;
import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisEnchantingTableTile;
import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisenchantMode;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModMenus;
import io.github.jason13official.disenchanting_table.impl.common.util.ExperienceHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class DisEnchantingMenu extends AbstractContainerMenu {

  private static final int SLOT_COUNT = 3;
  private static final int INV_SLOT_START = SLOT_COUNT;
  private static final int INV_SLOT_END = INV_SLOT_START + 27;
  private static final int USE_ROW_SLOT_START = INV_SLOT_END;
  private static final int USE_ROW_SLOT_END = USE_ROW_SLOT_START + 9;

  private final Container container;
  private final ContainerData data;

  public DisEnchantingMenu(int containerId, Inventory inventory) {
    this(containerId, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DisEnchantingTableTile.NUM_DATA_VALUES));
  }

  public DisEnchantingMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
    super(ModMenus.DISENCHANTING_TABLE, containerId);
    checkContainerSize(container, SLOT_COUNT);
    checkContainerDataCount(data, DisEnchantingTableTile.NUM_DATA_VALUES);
    this.container = container;
    this.data = data;

    this.addSlot(new InputSlot(container, 0, 27, 47));
    this.addSlot(new ExtraSlot(container, 1, 76, 47));
    this.addSlot(new OutputSlot(container, 2, 134, 47, this));

    this.addDataSlots(data);
    this.addStandardInventorySlots(inventory, 8, 84);
  }

  @Override
  public void broadcastChanges() {
    if (this.container instanceof DisEnchantingTableTile tile) {
      var level = tile.getLevel();
      if (level != null && !level.isClientSide() && tile.getMode() == DisenchantMode.MANUAL) {
        ItemStack input = tile.getItem(0);
        ItemStack extra = tile.getItem(1);
        boolean canProduce = !input.isEmpty() && !extra.isEmpty() && extra.is(Items.BOOK) && tile.isValidInput(input);
        ItemStack expected = canProduce ? tile.buildOutput(input) : ItemStack.EMPTY;
        if (!ItemStack.matches(tile.getItem(2), expected)) {
          tile.setItem(2, expected);
          tile.setChanged();
        }
      }
    }
    super.broadcastChanges();
  }

  public int getProgress() {
    return this.data.get(DisEnchantingTableTile.DATA_PROGRESS);
  }

  public int getMaxProgress() {
    return this.data.get(DisEnchantingTableTile.DATA_MAX_PROGRESS);
  }

  public DisenchantMode getMode() {
    return DisenchantMode.fromInt(this.data.get(DisEnchantingTableTile.DATA_MODE));
  }

  public Container getContainer() {
    return this.container;
  }

  @Override
  public boolean stillValid(Player player) {
    return this.container.stillValid(player);
  }

  @Override
  public ItemStack quickMoveStack(Player player, int slotIndex) {
    ItemStack clicked = ItemStack.EMPTY;
    Slot slot = this.slots.get(slotIndex);
    if (slot != null && slot.hasItem()) {
      ItemStack stack = slot.getItem();
      clicked = stack.copy();
      if (slotIndex < SLOT_COUNT) {
        if (!this.moveItemStackTo(stack, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
          return ItemStack.EMPTY;
        }
        slot.onQuickCraft(stack, clicked);
      } else {
        if (!this.moveItemStackTo(stack, 0, SLOT_COUNT, false)) {
          if (slotIndex < INV_SLOT_END) {
            if (!this.moveItemStackTo(stack, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
              return ItemStack.EMPTY;
            }
          } else if (!this.moveItemStackTo(stack, INV_SLOT_START, INV_SLOT_END, false)) {
            return ItemStack.EMPTY;
          }
        }
      }

      if (stack.isEmpty()) {
        slot.setByPlayer(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }

      if (stack.getCount() == clicked.getCount()) {
        return ItemStack.EMPTY;
      }

      slot.onTake(player, clicked);
    }

    return clicked;
  }

  private static class InputSlot extends Slot {
    InputSlot(Container container, int slot, int x, int y) {
      super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
      if (stack.is(Items.ENCHANTED_BOOK)) {
        return EnchantmentHelper.getEnchantmentsForCrafting(stack).size() >= 2;
      }
      return !EnchantmentHelper.getEnchantmentsForCrafting(stack).isEmpty();
    }
  }

  private static class ExtraSlot extends Slot {
    ExtraSlot(Container container, int slot, int x, int y) {
      super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
      return stack.is(Items.BOOK);
    }
  }

  private static class OutputSlot extends Slot {
    private final DisEnchantingMenu menu;

    OutputSlot(Container container, int slot, int x, int y, DisEnchantingMenu menu) {
      super(container, slot, x, y);
      this.menu = menu;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
      return false;
    }

    @Override
    public boolean mayPickup(Player player) {
      if (menu.container instanceof DisEnchantingTableTile tile
          && tile.getMode() == DisenchantMode.MANUAL
          && ModConfig.get().requiresExperience) {
        int cost = DisEnchantingTableTile.computeXpCost();
        return ModConfig.get().usesPoints
            ? ExperienceHelper.hasEnoughExperiencePoints(player, cost)
            : ExperienceHelper.hasEnoughExperienceLevels(player, cost);
      }
      return true;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
      if (menu.container instanceof DisEnchantingTableTile tile
          && tile.getMode() == DisenchantMode.MANUAL) {
        int cost = DisEnchantingTableTile.computeXpCost();
        if (ModConfig.get().requiresExperience) {
          if (ModConfig.get().usesPoints) {
            ExperienceHelper.deductExperiencePoints(player, cost);
          } else {
            ExperienceHelper.deductExperienceLevels(player, cost);
          }
        }
        tile.playCompletionEffect();
        tile.consumeInputs();
        tile.setChanged();
      }
      super.onTake(player, stack);
    }
  }
}
