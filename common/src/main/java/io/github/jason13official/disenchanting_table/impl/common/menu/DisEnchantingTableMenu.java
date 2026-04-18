package io.github.jason13official.disenchanting_table.impl.common.menu;

import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisenchantingTableTile;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModMenus;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class DisEnchantingTableMenu extends AbstractContainerMenu {

  private final Container container;
  private final ContainerData data;
  private final Level level;

  public DisEnchantingTableMenu(int containerId, Inventory inventory) {
    this(containerId, inventory, new SimpleContainer(3), new SimpleContainerData(1));
  }

  public DisEnchantingTableMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
    super(ModMenus.DISENCHANTING_TABLE, containerId);
    checkContainerSize(container, 3);
    checkContainerDataCount(data, 1);
    this.container = container;
    this.data = data;
    this.level = inventory.player.level();

    this.addSlot(new Slot(container, DisenchantingTableTile.INPUT_SLOT, 27, 47) {

      @Override
      public boolean mayPlace(ItemStack stack) {
        return DisEnchantingTableMenu.canPlaceItem(DisenchantingTableTile.INPUT_SLOT, stack);
      }
    });
    this.addSlot(new Slot(container, DisenchantingTableTile.EXTRA_SLOT, 76, 47) {

      @Override
      public boolean mayPlace(ItemStack stack) {
        return DisEnchantingTableMenu.canPlaceItem(DisenchantingTableTile.EXTRA_SLOT, stack);
      }
    });
    this.addSlot(new Slot(container, DisenchantingTableTile.OUTPUT_SLOT, 134, 47) {

      @Override
      public boolean mayPlace(ItemStack stack) {
        return DisEnchantingTableMenu.canPlaceItem(DisenchantingTableTile.OUTPUT_SLOT, stack);
      }
    });

    this.createInventorySlots(inventory);
  }

  private void createInventorySlots(Inventory inventory) {
    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < 9; ++j) {
        this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }
    }

    for (int k = 0; k < 9; ++k) {
      this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
    }
  }

  @Override
  public boolean stillValid(Player player) {
    return !player.isDeadOrDying();
  }

  //  @Override
//  protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
//
//    ItemCombinerMenuSlotDefinition.Builder definition = ItemCombinerMenuSlotDefinition.create();
//
//    definition.withSlot(0, 27, 47, this::mayDisenchant); // allow enchanted items or enchanted books with > 1 enchantment
//    definition.withSlot(1, 76, 47, stack -> stack.getItem() instanceof BookItem); // allow normal books
//    definition.withResultSlot(2, 134, 47);
//
//    return definition.build();
//  }

  /// return true if more than 1 enchantment or normal item with any enchantments
  public static boolean mayDisenchant(ItemStack stack) {
    int count = EnchantmentHelper.getEnchantments(stack).size();
    return count > 1 || (!(stack.getItem() instanceof EnchantedBookItem) && count > 0);
  }

  public static boolean canPlaceItem(int slot, ItemStack stack) {
    if (slot == DisenchantingTableTile.INPUT_SLOT && DisEnchantingTableMenu.mayDisenchant(stack)) {
      return true;
    }

    return slot == DisenchantingTableTile.EXTRA_SLOT && stack.getItem() instanceof BookItem;
  }

  // adapted from AbstractFurnaceMenu I think bc we have same amount of slots and i'm lazy
  public ItemStack quickMoveStack(Player player, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);
    if (slot != null && slot.hasItem()) {
      ItemStack itemstack1 = slot.getItem();
      itemstack = itemstack1.copy();
      if (index == 2) {
        if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
          return ItemStack.EMPTY;
        }

        slot.onQuickCraft(itemstack1, itemstack);
      } else if (index != 1 && index != 0) {
        if (this.mayDisenchant(itemstack1)) {
          if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
            return ItemStack.EMPTY;
          }
        } else if (itemstack1.getItem() instanceof BookItem) {
          if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
            return ItemStack.EMPTY;
          }
        } else if (index >= 3 && index < 30) {
          if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
            return ItemStack.EMPTY;
          }
        } else if (index >= 30 && index < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.setByPlayer(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }

      if (itemstack1.getCount() == itemstack.getCount()) {
        return ItemStack.EMPTY;
      }

      slot.onTake(player, itemstack1);
    }

    return itemstack;
  }
}
