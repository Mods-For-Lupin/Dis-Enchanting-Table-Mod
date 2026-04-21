package io.github.jason13official.disenchanting_table.platform;

import io.github.jason13official.disenchanting_table.platform.services.IRegistryHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.level.ItemLike;

public class NeoForgeRegistryHelper implements IRegistryHelper {

  @Override
  public Builder tabBuilder(ItemLike... tabItems) {
    return CreativeModeTab.builder().displayItems((itemDisplayParameters, output) -> {
      for (ItemLike tabItem : tabItems) {
        output.accept(tabItem);
      }
    });
  }
}
