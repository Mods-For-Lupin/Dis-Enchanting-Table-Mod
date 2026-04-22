package io.github.jason13official.disenchanting_table;

import io.github.jason13official.disenchanting_table.impl.client.renderer.DisEnchantingTableRenderer;
import io.github.jason13official.disenchanting_table.impl.client.screen.DisEnchantingScreen;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModMenus;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModTiles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;

public class DisEnchantingTableClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {

    DisEnchantingTableClient.packetSender = ClientPlayNetworking::send;
    DisEnchantingTableClient.init();

    BlockEntityRendererRegistry.register(ModTiles.DISENCHANTING_TABLE, DisEnchantingTableRenderer::new);
    MenuScreens.register(ModMenus.DISENCHANTING_TABLE, DisEnchantingScreen::new);
  }
}
