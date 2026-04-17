package io.github.jason13official.disenchanting_table.impl.common.registry;

import io.github.jason13official.disenchanting_table.Constants;
import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisenchantingTableTile;
import io.github.jason13official.disenchanting_table.platform.Services;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModTiles {

  public static BlockEntityType<DisenchantingTableTile> DISENCHANTING_TABLE;

  public static void register(BiConsumer<BlockEntityType<?>, ResourceLocation> consumer) {

    // DISENCHANTING_TABLE = BlockEntityType.Builder.of(DisenchantingTableTile::new, ModBlocks.DISENCHANTING_TABLE);
    DISENCHANTING_TABLE = Services.registry().createTile(DisenchantingTableTile::new, ModBlocks.DISENCHANTING_TABLE);

    consumer.accept(DISENCHANTING_TABLE, DisEnchantingTable.identifier(Constants.MOD_ID));
  }
}
