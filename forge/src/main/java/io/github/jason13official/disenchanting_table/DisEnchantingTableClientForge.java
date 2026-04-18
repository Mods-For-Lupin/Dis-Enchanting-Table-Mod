package io.github.jason13official.disenchanting_table;

import io.github.jason13official.disenchanting_table.impl.client.gui.screens.inventory.DisEnchantingTableScreen;
import io.github.jason13official.disenchanting_table.impl.client.renderer.blockentity.DisEnchantingTableTileRenderer;
import io.github.jason13official.disenchanting_table.impl.client.renderer.blockentity.DisEnchantingTableTileRenderer.CrystalModel;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModMenus;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModTiles;
import java.util.function.Consumer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class DisEnchantingTableClientForge {

  public DisEnchantingTableClientForge(final IEventBus modEventBus) {

    modEventBus.addListener((Consumer<FMLClientSetupEvent>) event -> {

      DisEnchantingTableClient.init();
      MenuScreens.register(ModMenus.DISENCHANTING_TABLE, DisEnchantingTableScreen::new);
    });

    modEventBus.addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> {
      event.registerBlockEntityRenderer(ModTiles.DISENCHANTING_TABLE, DisEnchantingTableTileRenderer::new);
    });

    modEventBus.addListener((Consumer<EntityRenderersEvent.RegisterLayerDefinitions>) event -> {
      event.registerLayerDefinition(CrystalModel.LAYER_LOCATION, CrystalModel::createBodyLayer);
    });
  }
}
