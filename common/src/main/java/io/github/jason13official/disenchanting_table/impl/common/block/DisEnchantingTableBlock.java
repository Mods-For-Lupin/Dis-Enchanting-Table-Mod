package io.github.jason13official.disenchanting_table.impl.common.block;

import com.mojang.serialization.MapCodec;
import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisEnchantingTableTile;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

/// [EnchantingTableBlock] [CampfireBlock]
public class DisEnchantingTableBlock extends DirectionalTableBlock implements EntityBlock {

  public static final MapCodec<DisEnchantingTableBlock> CODEC = simpleCodec(DisEnchantingTableBlock::new);

  public DisEnchantingTableBlock(Properties properties) {
    super(properties);
  }

  @SuppressWarnings("unchecked")
  protected static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> actual, BlockEntityType<E> expected,
      BlockEntityTicker<? super E> ticker) {
    return expected == actual ? (BlockEntityTicker<A>) ticker : null;
  }

  @Override
  protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
    return CODEC;
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
    if (!level.isClientSide()) {
      this.openContainer(level, pos, player);
    }

    return InteractionResult.SUCCESS;
  }

  protected void openContainer(Level level, BlockPos pos, Player player) {
    BlockEntity blockEntity = level.getBlockEntity(pos);
    if (blockEntity instanceof DisEnchantingTableTile) {
      player.openMenu((MenuProvider) blockEntity);
    }
  }

  @Override
  protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
    BlockEntity blockEntity = level.getBlockEntity(pos);
    return blockEntity instanceof MenuProvider ? (MenuProvider) blockEntity : null;
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new DisEnchantingTableTile(blockPos, blockState);
  }

  public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
    return level.isClientSide() ? createTickerHelper(type, ModTiles.DISENCHANTING_TABLE, DisEnchantingTableTile::tickClient)
        : createTickerHelper(type, ModTiles.DISENCHANTING_TABLE, DisEnchantingTableTile::tickServer);
  }
}
