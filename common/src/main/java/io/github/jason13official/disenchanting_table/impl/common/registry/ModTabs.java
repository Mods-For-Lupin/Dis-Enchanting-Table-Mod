package io.github.jason13official.disenchanting_table.impl.common.registry;

import io.github.jason13official.disenchanting_table.Constants;
import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import io.github.jason13official.disenchanting_table.platform.Services;
import java.util.function.BiConsumer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModTabs {

  public static CreativeModeTab DISENCHANTING_TABLE;

  public static void register(BiConsumer<CreativeModeTab, Identifier> consumer) {

    DISENCHANTING_TABLE = Services.registry().tabBuilder(ModItems.DISENCHANTING_TABLE).icon(() -> new ItemStack(ModItems.DISENCHANTING_TABLE))
        .title(Component.translatable("itemGroup.disenchantingTable")).build();

    consumer.accept(DISENCHANTING_TABLE, DisEnchantingTable.identifier(Constants.MOD_ID));
  }
}
