package io.github.jason13official.disenchanting_table.platform.services;

import java.util.Set;
import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface IRegistryHelper {

  CreativeModeTab.Builder tabBuilder(ItemLike... tabItems);

  <T extends BlockEntity> BlockEntityType<T> tile(BiFunction<BlockPos, BlockState, T> constructor, Set<Block> validBlocks);
}
