package io.github.jason13official.disenchanting_table.platform;

import io.github.jason13official.disenchanting_table.platform.services.IRegistryHelper;
import java.util.function.BiFunction;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;
import net.minecraft.world.level.block.state.BlockState;

public class FabricRegistryHelper implements IRegistryHelper {

  @Override
  public <T extends BlockEntity> BlockEntityType<T> createTile(BiFunction<BlockPos, BlockState, T> factory, Block... validBlocks) {
    return FabricBlockEntityTypeBuilder.create(factory::apply, validBlocks).build();
  }

  @Override
  public <T extends AbstractContainerMenu> MenuType<T> createMenu(BiFunction<Integer, Inventory, T> factory) {
    return new MenuType<T>(factory::apply, FeatureFlags.VANILLA_SET);
  }
}
