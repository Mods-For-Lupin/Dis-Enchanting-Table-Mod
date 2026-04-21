package io.github.jason13official.disenchanting_table.impl.common.registry;

import io.github.jason13official.disenchanting_table.Constants;
import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import io.github.jason13official.disenchanting_table.impl.common.block.DisEnchantingTableBlock;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {

  public static Block DISENCHANTING_TABLE;

  public static void register(BiConsumer<Block, Identifier> consumer) {

    DISENCHANTING_TABLE = create(Constants.MOD_ID, DisEnchantingTableBlock::new, Properties.ofFullCopy(Blocks.ENCHANTING_TABLE).mapColor(MapColor.COLOR_BLUE));

    consumer.accept(DISENCHANTING_TABLE, DisEnchantingTable.identifier(Constants.MOD_ID));
  }

  private static Block create(String id, Function<Properties, Block> constructor, Properties properties) {
    var key = ResourceKey.create(Registries.BLOCK, DisEnchantingTable.identifier(id));
    return constructor.apply(properties.setId(key));
  }
}
