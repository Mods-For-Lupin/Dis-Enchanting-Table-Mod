package io.github.jason13official.disenchanting_table.platform.services;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

public interface IRegistryHelper {

  CreativeModeTab.Builder tabBuilder(ItemLike... tabItems);
}
