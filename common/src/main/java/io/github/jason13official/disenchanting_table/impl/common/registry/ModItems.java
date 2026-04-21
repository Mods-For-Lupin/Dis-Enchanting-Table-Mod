package io.github.jason13official.disenchanting_table.impl.common.registry;

import io.github.jason13official.disenchanting_table.Constants;
import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModItems {

  public static Item DISENCHANTING_TABLE;

  public static void register(BiConsumer<Item, Identifier> consumer) {

    // DISENCHANTING_TABLE = new BlockItem(ModBlocks.DISENCHANTING_TABLE, new Properties());
    DISENCHANTING_TABLE = create("disenchanting_table", properties -> new BlockItem(ModBlocks.DISENCHANTING_TABLE, properties), new Properties());

    consumer.accept(DISENCHANTING_TABLE, DisEnchantingTable.identifier(Constants.MOD_ID));
  }

  private static Item create(String id, Function<Properties, Item> constructor, Properties properties) {
    var key = ResourceKey.create(Registries.ITEM, DisEnchantingTable.identifier(id));
    return constructor.apply(properties.setId(key));
  }
}
