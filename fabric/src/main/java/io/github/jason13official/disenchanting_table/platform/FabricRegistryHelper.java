package io.github.jason13official.disenchanting_table.platform;

import io.github.jason13official.disenchanting_table.platform.services.IRegistryHelper;
import java.util.Set;
import java.util.function.BiFunction;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.impl.object.builder.ExtendedBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FabricRegistryHelper implements IRegistryHelper {

  @Override
  public Builder tabBuilder(ItemLike... tabItems) {
    return FabricCreativeModeTab.builder().displayItems((parameters, output) -> {
      for (ItemLike tabItem : tabItems) {
        output.accept(tabItem);
      }
    });
  }

  @Override
  public <T extends BlockEntity> BlockEntityType<T> tile(BiFunction<BlockPos, BlockState, T> constructor, Set<Block> validBlocks) {
    // return new ExtendedBlockEntityType<T>(constructor::apply, validBlocks, false);
    return new BlockEntityType<T>(constructor::apply, validBlocks);
  }
}
