package io.github.jason13official.disenchanting_table.platform.services;

import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface IRegistryHelper {

  <T extends BlockEntity> BlockEntityType<T> createTile(BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks);

  <T extends AbstractContainerMenu> MenuType<T> createMenu(BiFunction<Integer, Inventory, T> factory);
}
