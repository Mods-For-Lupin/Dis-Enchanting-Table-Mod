package io.github.jason13official.disenchanting_table.impl.common.registry;

import io.github.jason13official.disenchanting_table.Constants;
import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import io.github.jason13official.disenchanting_table.platform.Services;
import java.util.function.BiConsumer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModTabs {

  public static CreativeModeTab DISENCHANTING_TABLE;

  public static void register(BiConsumer<CreativeModeTab, ResourceLocation> consumer) {

    DISENCHANTING_TABLE = Services.PLATFORM.tabBuilder()
        .icon(() -> new ItemStack(ModItems.DISENCHANTING_TABLE))
        .title(Component.translatable("itemGroup.disenchantingTable"))
        .displayItems(((itemDisplayParameters, output) -> output.accept(ModItems.DISENCHANTING_TABLE)))
        .build();

    consumer.accept(DISENCHANTING_TABLE, DisEnchantingTable.identifier(Constants.MOD_ID));
  }
}
