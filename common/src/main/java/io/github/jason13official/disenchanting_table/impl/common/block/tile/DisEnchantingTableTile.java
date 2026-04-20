package io.github.jason13official.disenchanting_table.impl.common.block.tile;

import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingTableMenu;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class DisEnchantingTableTile extends BaseContainerBlockEntity implements WorldlyContainer {

  public static final int INPUT_SLOT = 0;
  public static final int EXTRA_SLOT = 1;
  public static final int OUTPUT_SLOT = 2;
  public static final int SLOT_COUNT = 3;

  public static final int MODE_MANUAL = 0;
  public static final int MODE_AUTO = 1;

  /// into block from above -> first two slots
  public static final int[] SLOTS_FOR_UP = new int[]{INPUT_SLOT, EXTRA_SLOT};

  ///  into block from sides -> first two slots
  public static final int[] SLOTS_FOR_SIDES = new int[]{INPUT_SLOT, EXTRA_SLOT};

  /// into block from below -> output slot only
  public static final int[] SLOTS_FOR_DOWN = new int[]{OUTPUT_SLOT};

  protected final ContainerData dataAccess;

  private NonNullList<ItemStack> items;

  private int mode = MODE_MANUAL;

  public int time;
  public float open;
  public float oOpen;

  public DisEnchantingTableTile(BlockPos pos, BlockState blockState) {
    super(ModTiles.DISENCHANTING_TABLE, pos, blockState);

    this.items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    this.dataAccess = new ContainerData() {
      @Override
      public int get(int index) {
        return index == 0 ? DisEnchantingTableTile.this.mode : 0;
      }

      @Override
      public void set(int index, int value) {
        if (index == 0) {
          DisEnchantingTableTile.this.mode = value;
        }
      }

      @Override
      public int getCount() {
        return 1;
      }
    };
  }

  public int getMode() {
    return mode;
  }

  public void setMode(int mode) {
    this.mode = (mode == MODE_AUTO) ? MODE_AUTO : MODE_MANUAL;
    this.setChanged();
  }

  public static void tickClient(Level level, BlockPos pos, BlockState state, DisEnchantingTableTile tile) {
    tile.oOpen = tile.open;
    Player player = level.getNearestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3.0, false);
    tile.open = Mth.clamp(tile.open + (player != null ? 0.1F : -0.1F), 0.0F, 1.0F);
    tile.time++;
  }

  public static void tickServer(Level level, BlockPos pos, BlockState state, DisEnchantingTableTile tile) {
    ItemStack input = tile.getItem(INPUT_SLOT);
    ItemStack book = tile.getItem(EXTRA_SLOT);

    if (tile.mode == MODE_AUTO) {
      // auto: eagerly consume inputs and produce output so hoppers can simply extract
      if (!input.isEmpty() && !book.isEmpty()
          && book.getItem() instanceof BookItem
          && DisEnchantingTableMenu.mayDisenchant(input)
          && tile.getItem(OUTPUT_SLOT).isEmpty()) {
        performDisenchant(tile);
      }
    } else {
      // manual: show a preview in the output slot without consuming inputs;
      // actual consumption happens in the menu when the player takes the result
      ItemStack expectedOutput = computePreview(input, book);
      ItemStack currentOutput = tile.getItem(OUTPUT_SLOT);
      if (!ItemStack.matches(currentOutput, expectedOutput)) {
        tile.items.set(OUTPUT_SLOT, expectedOutput);
        tile.markUpdated();
      }
    }
  }

  /// computes the enchanted book that would be produced without modifying any state
  private static ItemStack computePreview(ItemStack input, ItemStack book) {
    if (input.isEmpty() || book.isEmpty() || !(book.getItem() instanceof BookItem)
        || !DisEnchantingTableMenu.mayDisenchant(input)) {
      return ItemStack.EMPTY;
    }
    var enchants = EnchantmentHelper.getEnchantments(input);
    if (enchants.isEmpty()) return ItemStack.EMPTY;
    var entry = enchants.entrySet().iterator().next();
    ItemStack outputBook = new ItemStack(Items.ENCHANTED_BOOK);
    EnchantedBookItem.addEnchantment(outputBook, new EnchantmentInstance(entry.getKey(), entry.getValue()));
    return outputBook;
  }

  /// consumes inputs and writes the result to OUTPUT_SLOT (auto mode / internal use)
  private static void performDisenchant(DisEnchantingTableTile tile) {
    ItemStack input = tile.getItem(INPUT_SLOT);
    Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(input);
    if (enchants.isEmpty()) return;
    var entry = enchants.entrySet().iterator().next();

    ItemStack outputBook = new ItemStack(Items.ENCHANTED_BOOK);
    EnchantedBookItem.addEnchantment(outputBook, new EnchantmentInstance(entry.getKey(), entry.getValue()));

    enchants.remove(entry.getKey());
    if (input.is(Items.ENCHANTED_BOOK)) input.removeTagKey("StoredEnchantments");
    EnchantmentHelper.setEnchantments(enchants, input);

    tile.getItem(EXTRA_SLOT).shrink(1);
    tile.items.set(OUTPUT_SLOT, outputBook);
    tile.markUpdated();
  }

  /// called by the menu when the player takes from the output slot in manual mode;
  /// strips the enchantment from the input and consumes the book without touching the output slot
  public static void consumeForManualTake(DisEnchantingTableTile tile) {
    ItemStack input = tile.getItem(INPUT_SLOT);
    if (input.isEmpty()) return;
    Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(input);
    if (enchants.isEmpty()) return;
    var entry = enchants.entrySet().iterator().next();
    enchants.remove(entry.getKey());
    if (input.is(Items.ENCHANTED_BOOK)) input.removeTagKey("StoredEnchantments");
    EnchantmentHelper.setEnchantments(enchants, input);
    tile.getItem(EXTRA_SLOT).shrink(1);
    tile.markUpdated();
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
    return slot == OUTPUT_SLOT && this.mode == MODE_AUTO;
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    ContainerHelper.loadAllItems(tag, this.items);
    this.mode = tag.getInt("Mode");
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    ContainerHelper.saveAllItems(tag, this.items, true);
    tag.putInt("Mode", this.mode);
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = new CompoundTag();
    ContainerHelper.saveAllItems(tag, this.items, true);
    return tag;
  }

  private void markUpdated() {
    this.setChanged();
    this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    return DisEnchantingTableMenu.canPlaceItem(slot, stack);
  }
}
