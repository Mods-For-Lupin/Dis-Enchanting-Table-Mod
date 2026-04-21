package io.github.jason13official.disenchanting_table.impl.common.block.tile;

import io.github.jason13official.disenchanting_table.impl.common.registry.ModTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DisEnchantingTableTile extends AbstractDisEnchantingTile {

  public DisEnchantingTableTile(BlockPos worldPosition, BlockState blockState) {
    super(ModTiles.DISENCHANTING_TABLE, worldPosition, blockState);
  }

  public static void tickClient(Level level, BlockPos blockPos, BlockState state, DisEnchantingTableTile tile) {

  }

  public static void tickServer(Level level, BlockPos blockPos, BlockState state, DisEnchantingTableTile tile) {

  }
}
