package io.github.jason13official.disenchanting_table;

import io.github.jason13official.disenchanting_table.impl.client.renderer.DisEnchantingTableRenderer;
import io.github.jason13official.disenchanting_table.impl.client.screen.DisEnchantingScreen;
import io.github.jason13official.disenchanting_table.impl.common.ModConfig;
import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisEnchantingTableTile;
import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingMenu;
import io.github.jason13official.disenchanting_table.impl.common.network.ConfigSyncS2CPacket;
import io.github.jason13official.disenchanting_table.impl.common.network.ModePacket;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModMenus;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModTiles;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class DisEnchantingTableClientNeoForge {

  public DisEnchantingTableClientNeoForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> {
      DisEnchantingTableClient.serverBoundPacketSender = ClientPacketDistributor::sendToServer;
      DisEnchantingTableClient.init();
    });

    modEventBus.addListener((Consumer<RegisterMenuScreensEvent>) event -> {
      event.register(ModMenus.DISENCHANTING_TABLE, DisEnchantingScreen::new);
    });

    modEventBus.addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event ->
        event.registerBlockEntityRenderer(ModTiles.DISENCHANTING_TABLE, DisEnchantingTableRenderer::new));

    modEventBus.addListener((Consumer<RegisterPayloadHandlersEvent>) event -> {
      PayloadRegistrar registrar = event.registrar(Constants.MOD_ID);
      registrar.playToClient(ConfigSyncS2CPacket.TYPE, ConfigSyncS2CPacket.STREAM_CODEC, (payload, context) -> {
        context.enqueueWork(() -> {
          ModConfig.get().sync(payload);
        });
      });
    });

    NeoForge.EVENT_BUS.addListener((Consumer<EntityLeaveLevelEvent>) event -> {
      if (Minecraft.getInstance().player != null && event.getEntity() == Minecraft.getInstance().player) {
        ModConfig.unsync();
      }
    });
  }
}
