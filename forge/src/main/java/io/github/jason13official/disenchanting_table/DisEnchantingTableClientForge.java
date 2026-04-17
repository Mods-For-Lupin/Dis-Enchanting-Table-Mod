package io.github.jason13official.disenchanting_table;

import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class DisEnchantingTableClientForge {

  public DisEnchantingTableClientForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> DisEnchantingTableClient.init());
  }
}
