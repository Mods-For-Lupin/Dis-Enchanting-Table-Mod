package io.github.jason13official.disenchanting_table.platform;

import io.github.jason13official.disenchanting_table.platform.services.IRegistryHelper;
import java.util.Set;
import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class NeoForgeRegistryHelper implements IRegistryHelper {

  @Override
  public Builder tabBuilder(ItemLike... tabItems) {
    return CreativeModeTab.builder().displayItems((itemDisplayParameters, output) -> {
      for (ItemLike tabItem : tabItems) {
        output.accept(tabItem);
      }
    });
  }

  @Override
  public <T extends BlockEntity> BlockEntityType<T> tile(BiFunction<BlockPos, BlockState, T> constructor, Set<Block> validBlocks) {
    return new BlockEntityType<T>(constructor::apply, validBlocks);
  }

  @Override
  public <T extends AbstractContainerMenu> MenuType<T> menu(BiFunction<Integer, Inventory, T> constructor, FeatureFlagSet requiredFeatures) {
    return new MenuType<>(constructor::apply, requiredFeatures);
  }
}
