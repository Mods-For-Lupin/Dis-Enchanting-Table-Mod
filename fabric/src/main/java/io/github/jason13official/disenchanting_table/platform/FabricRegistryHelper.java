package io.github.jason13official.disenchanting_table.platform;

import io.github.jason13official.disenchanting_table.platform.services.IRegistryHelper;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.level.ItemLike;

public class FabricRegistryHelper implements IRegistryHelper {

  @Override
  public Builder tabBuilder(ItemLike... tabItems) {
    return FabricCreativeModeTab.builder().displayItems((parameters, output) -> {
      for (ItemLike tabItem : tabItems) {
        output.accept(tabItem);
      }
    });
  }
}
