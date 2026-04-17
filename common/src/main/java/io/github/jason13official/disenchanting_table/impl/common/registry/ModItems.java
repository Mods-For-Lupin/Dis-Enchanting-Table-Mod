package io.github.jason13official.disenchanting_table.impl.common.registry;

import io.github.jason13official.disenchanting_table.Constants;
import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class ModItems {

  public static Item DISENCHANTING_TABLE;

  public static void register(BiConsumer<Item, ResourceLocation> consumer) {

    DISENCHANTING_TABLE = new BlockItem(ModBlocks.DISENCHANTING_TABLE, new Item.Properties());

    consumer.accept(DISENCHANTING_TABLE, DisEnchantingTable.identifier(Constants.MOD_ID));
  }
}
