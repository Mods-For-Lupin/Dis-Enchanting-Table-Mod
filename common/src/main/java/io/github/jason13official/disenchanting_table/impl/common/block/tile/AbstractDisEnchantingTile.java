package io.github.jason13official.disenchanting_table.impl.common.block.tile;

import io.github.jason13official.disenchanting_table.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

/// [CampfireBlockEntity]
public abstract class AbstractDisEnchantingTile extends BlockEntity implements Container, WorldlyContainer, Clearable {

  private static final int[] SLOTS_FOR_UP = new int[] {0, 1};
  private static final int[] SLOTS_FOR_DOWN = new int[] {2};
  private static final int[] SLOTS_FOR_SIDES = new int[] {0, 1};

  private final NonNullList<ItemStack> items;

  public AbstractDisEnchantingTile(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
    super(type, worldPosition, blockState);
    this.items = NonNullList.withSize(3, ItemStack.EMPTY);
  }

  /// mimics [CampfireBlockEntity] so we're ignoring the possible null pointer exception
  private void markUpdated() {
    this.setChanged();
    this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
  }

  @Override
  public void clearContent() {
    this.items.clear();
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    this.clearContent();
    ContainerHelper.loadAllItems(input, this.items);
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    ContainerHelper.saveAllItems(output, this.items, true);
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag(Provider registries) {
    try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this.problemPath(), Constants.LOG)) {
      TagValueOutput output = TagValueOutput.createWithContext(reporter, registries);
      ContainerHelper.saveAllItems(output, this.items, true);
      return output.buildResult();
    }
  }

  /// removes the tile's items when adding block data to an item in [ServerGamePacketListenerImpl]
  @Override
  public void removeComponentsFromTag(ValueOutput output) {
    output.discard("Items");
  }

  @Override
  public int[] getSlotsForFace(Direction direction) {
    return switch (direction) {
      case UP -> SLOTS_FOR_UP;
      case DOWN -> SLOTS_FOR_DOWN;
      default -> SLOTS_FOR_SIDES;
    };
  }

  @Override
  public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
    return true; // TODO update
  }

  @Override
  public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    return true; // TODO update
  }

  @Override
  public int getContainerSize() {
    return this.items.size();
  }

  @Override
  public boolean isEmpty() {
    return this.items.isEmpty();
  }

  @Override
  public ItemStack getItem(int i) {
    return this.items.get(i);
  }

  @Override
  public ItemStack removeItem(int i, int i1) {
    return ContainerHelper.removeItem(this.items, i, i1);
  }

  @Override
  public ItemStack removeItemNoUpdate(int i) {
    return ContainerHelper.takeItem(this.items, i);
  }

  @Override
  public void setItem(int i, ItemStack itemStack) {
    this.items.set(i, itemStack);
  }

  @Override
  public boolean stillValid(Player player) {
    return !player.isDeadOrDying();
  }
}
