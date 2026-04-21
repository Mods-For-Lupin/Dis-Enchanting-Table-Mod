package io.github.jason13official.disenchanting_table;

import io.github.jason13official.disenchanting_table.impl.client.screen.DisEnchantingScreen;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModMenus;
import java.util.function.Consumer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class DisEnchantingTableClientNeoForge {

  public DisEnchantingTableClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> {
      DisEnchantingTableClient.packetSender = ClientPacketDistributor::sendToServer;
      DisEnchantingTableClient.init();
    });

    modEventBus.addListener((Consumer<RegisterMenuScreensEvent>) event -> {
      event.register(ModMenus.DISENCHANTING_TABLE, DisEnchantingScreen::new);
    });
  }
}
