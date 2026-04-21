package io.github.jason13official.disenchanting_table.impl.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

/// [EnchantingTableBlock]
public class DisEnchantingTableBlock extends DirectionalTableBlock {

  public static final MapCodec<DisEnchantingTableBlock> CODEC = simpleCodec(DisEnchantingTableBlock::new);

  public DisEnchantingTableBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
    return CODEC;
  }
}
