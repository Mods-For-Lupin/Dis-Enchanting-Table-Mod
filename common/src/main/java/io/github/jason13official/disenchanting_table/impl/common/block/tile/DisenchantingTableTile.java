package io.github.jason13official.disenchanting_table.impl.common.block.tile;

import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingTableMenu;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DisenchantingTableTile extends BaseContainerBlockEntity implements WorldlyContainer {

  public static final int INPUT_SLOT = 0;
  public static final int EXTRA_SLOT = 1;
  public static final int OUTPUT_SLOT = 2;
  public static final int SLOT_COUNT = 3;

  /// into block from above -> first two slots
  public static final int[] SLOTS_FOR_UP = new int[]{INPUT_SLOT, EXTRA_SLOT};

  ///  into block from sides -> first two slots
  public static final int[] SLOTS_FOR_SIDES = new int[]{INPUT_SLOT, EXTRA_SLOT};

  /// into block from below -> first and last slot
  public static final int[] SLOTS_FOR_DOWN = new int[]{INPUT_SLOT, OUTPUT_SLOT};

  protected final ContainerData dataAccess;

  private NonNullList<ItemStack> items;

  public int time;
  public float open;
  public float oOpen;

  public DisenchantingTableTile(BlockPos pos, BlockState blockState) {
    super(ModTiles.DISENCHANTING_TABLE, pos, blockState);

    this.items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    this.dataAccess = new ContainerData() {
      @Override
      public int get(int index) {
        return 0;
      }

      @Override
      public void set(int index, int value) {

      }

      @Override
      public int getCount() {
        return 1;
      }
    };
  }

  public static void tickClient(Level level, BlockPos pos, BlockState state, DisenchantingTableTile tile) {
    tile.oOpen = tile.open;
    Player player = level.getNearestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3.0, false);
    tile.open = Mth.clamp(tile.open + (player != null ? 0.1F : -0.1F), 0.0F, 1.0F);
    tile.time++;
  }

  public static void tickServer(Level level, BlockPos pos, BlockState state, DisenchantingTableTile tile) {

  }

  @Override
  protected Component getDefaultName() {
    return Component.translatable("itemGroup.disenchantingTable");
  }

  @Override
  protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
    return new DisEnchantingTableMenu(i, inventory, this, this.dataAccess);
  }

  @Override
  public int getContainerSize() {
    return 3;
  }

  @Override
  public boolean isEmpty() {
    for (ItemStack itemStack : this.items) {
      if (!itemStack.isEmpty()) {
        return false;
      }
    }

    return true;
  }

  @Override
  public ItemStack getItem(int slot) {
    return slot >= 0 && slot < SLOT_COUNT ? this.items.get(slot) : ItemStack.EMPTY;
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    return ContainerHelper.removeItem(this.items, slot, amount);
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    return ContainerHelper.takeItem(this.items, slot);
  }

  @Override
  public void setItem(int slot, ItemStack stack) {
    if (slot >= 0 && slot < this.items.size()) {
      this.items.set(slot, stack);
    }
  }

  @Override
  public boolean stillValid(Player player) {
    return Container.stillValidBlockEntity(this, player);
  }

  @Override
  public void clearContent() {
    this.items.clear();
  }

  @Override
  public int[] getSlotsForFace(Direction side) {

    return switch (side) {
      case UP -> SLOTS_FOR_UP;
      case DOWN -> SLOTS_FOR_DOWN;
      default -> SLOTS_FOR_SIDES;
    };
  }

  @Override
  public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction direction) {

    if (slot == INPUT_SLOT && DisEnchantingTableMenu.mayDisenchant(stack)) {
      return true;
    }

    return slot == EXTRA_SLOT && stack.getItem() instanceof BookItem;
  }

  @Override
  public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {

    if (slot == INPUT_SLOT) {
      if (stack.getItem() instanceof EnchantedBookItem && EnchantmentHelper.getEnchantments(stack).size() == 1) {
        return true;
      }
      else {
        return EnchantmentHelper.getEnchantments(stack).isEmpty();
      }
    }

    return slot == OUTPUT_SLOT;
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    ContainerHelper.loadAllItems(tag, this.items);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    ContainerHelper.saveAllItems(tag, this.items);
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    return DisEnchantingTableMenu.canPlaceItem(slot, stack);
  }
}
