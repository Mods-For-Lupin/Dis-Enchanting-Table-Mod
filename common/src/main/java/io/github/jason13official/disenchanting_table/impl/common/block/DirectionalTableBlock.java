package io.github.jason13official.disenchanting_table.impl.common.block;

import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisEnchantingTableTile;
import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisenchantMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LoomBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

/// [LoomBlock] [EnchantingTableBlock]
public abstract class DirectionalTableBlock extends HorizontalDirectionalBlock {

  private static final VoxelShape SHAPE = Block.column(16.0F, 0.0F, 12.0F);

  public DirectionalTableBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  @Override
  protected boolean useShapeForLightOcclusion(BlockState state) {
    return true;
  }

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType type) {
    return false;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Override
  public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {

    cleanup(level, pos);

    return super.playerWillDestroy(level, pos, state, player);
  }

  private static void cleanup(LevelAccessor level, BlockPos pos) {

    if (!level.isClientSide() && level.getBlockEntity(pos) instanceof DisEnchantingTableTile tile) {

      if (tile.getMode() == DisenchantMode.MANUAL) {

        // ignore fully disenchanted book (with 1 or fewer enchantments)
        if (tile.getItem(0).is(Items.ENCHANTED_BOOK) && EnchantmentHelper.getEnchantmentsForCrafting(tile.getItem(0)).size() < 2) {
          return;
        }

        // ignore fully disenchanted normal items
        if (EnchantmentHelper.getEnchantmentsForCrafting(tile.getItem(0)).isEmpty()) {
          return;
        }

        // so we remove output if we haven't fully disenchanted the item ?
        tile.setItem(2, ItemStack.EMPTY);
      }
    }
  }

  @Override
  public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {

    cleanup(level, pos);
    super.destroy(level, pos, state);
  }

  @Override
  public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack destroyedWith) {

    cleanup(level, pos);

    super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith);
  }
}
