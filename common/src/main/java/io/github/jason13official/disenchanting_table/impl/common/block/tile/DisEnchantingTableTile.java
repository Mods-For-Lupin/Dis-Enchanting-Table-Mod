package io.github.jason13official.disenchanting_table.impl.common.block.tile;

import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingMenu;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class DisEnchantingTableTile extends AbstractDisEnchantingTile implements MenuProvider {

  protected final ContainerData data;

  public DisEnchantingTableTile(BlockPos worldPosition, BlockState blockState) {
    super(ModTiles.DISENCHANTING_TABLE, worldPosition, blockState);
    this.data = new ContainerData() {
      @Override
      public int get(int i) {
        return 0;
      }

      @Override
      public void set(int i, int i1) {

      }

      @Override
      public int getCount() {
        return 1;
      }
    };
  }

  public static void tickClient(Level level, BlockPos blockPos, BlockState state, DisEnchantingTableTile tile) {

  }

  public static void tickServer(Level level, BlockPos blockPos, BlockState state, DisEnchantingTableTile tile) {

  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("itemGroup.disenchantingTable");
  }

  @Override
  public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
    return new DisEnchantingMenu(i, inventory, this, this.data);
  }
}
