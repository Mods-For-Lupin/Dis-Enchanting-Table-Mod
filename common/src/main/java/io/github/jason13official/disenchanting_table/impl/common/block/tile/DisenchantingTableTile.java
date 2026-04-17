package io.github.jason13official.disenchanting_table.impl.common.block.tile;

import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingTableMenu;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DisenchantingTableTile extends BaseContainerBlockEntity implements WorldlyContainer {

  protected final ContainerData dataAccess;

  public DisenchantingTableTile(BlockPos pos, BlockState blockState) {
    super(ModTiles.DISENCHANTING_TABLE, pos, blockState);

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

  }

  public static void tickServer(Level level, BlockPos pos, BlockState state, DisenchantingTableTile tile) {

  }

  @Override
  protected Component getDefaultName() {
    return Component.empty();
  }

  @Override
  protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
    return new DisEnchantingTableMenu(i, inventory, this, this.dataAccess);
  }

  @Override
  public int getContainerSize() {
    return 0;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public ItemStack getItem(int i) {
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack removeItem(int i, int i1) {
    return ItemStack.EMPTY;
  }

  @Override
  public ItemStack removeItemNoUpdate(int i) {
    return ItemStack.EMPTY;
  }

  @Override
  public void setItem(int i, ItemStack itemStack) {

  }

  @Override
  public boolean stillValid(Player player) {
    return false;
  }

  @Override
  public void clearContent() {

  }

  @Override
  public int[] getSlotsForFace(Direction direction) {
    return new int[0];
  }

  @Override
  public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return false;
  }

  @Override
  public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return false;
  }
}
