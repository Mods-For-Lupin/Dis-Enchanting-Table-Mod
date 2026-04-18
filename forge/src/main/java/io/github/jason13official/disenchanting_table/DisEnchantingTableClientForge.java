package io.github.jason13official.disenchanting_table;

import io.github.jason13official.disenchanting_table.impl.client.gui.screens.inventory.DisEnchantingTableScreen;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModMenus;
import java.util.function.Consumer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class DisEnchantingTableClientForge {

  public DisEnchantingTableClientForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> {

      DisEnchantingTableClient.init();
      MenuScreens.register(ModMenus.DISENCHANTING_TABLE, DisEnchantingTableScreen::new);
    });
  }
}
