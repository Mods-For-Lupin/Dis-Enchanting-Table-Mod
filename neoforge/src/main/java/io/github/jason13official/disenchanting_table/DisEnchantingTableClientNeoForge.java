package io.github.jason13official.disenchanting_table;

import java.util.function.Consumer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class DisEnchantingTableClientNeoForge {

  public DisEnchantingTableClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> DisEnchantingTableClient.init());
  }
}
