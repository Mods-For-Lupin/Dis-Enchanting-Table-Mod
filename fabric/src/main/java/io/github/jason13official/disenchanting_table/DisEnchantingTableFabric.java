package io.github.jason13official.disenchanting_table;

import io.github.jason13official.disenchanting_table.impl.common.ModConfig;
import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingMenu;
import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisEnchantingTableTile;
import io.github.jason13official.disenchanting_table.platform.Services;
import io.github.jason13official.disenchanting_table.impl.common.network.ModePacket;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModBlocks;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModEntities;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModItems;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModMenus;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModParticles;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModTabs;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModTiles;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.v1.DataResourceLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class DisEnchantingTableFabric implements ModInitializer {

  @Override
  public void onInitialize() {

    Constants.LOG.info("DisEnchantingTableFabric#onInitialize");

    bind(BuiltInRegistries.BLOCK, ModBlocks::register);
    bind(BuiltInRegistries.ENTITY_TYPE, ModEntities::register);
    bind(BuiltInRegistries.ITEM, ModItems::register);
    bind(BuiltInRegistries.PARTICLE_TYPE, ModParticles::register);
    bind(BuiltInRegistries.BLOCK_ENTITY_TYPE, ModTiles::register);
    bind(BuiltInRegistries.MENU, ModMenus::register);
    bind(BuiltInRegistries.CREATIVE_MODE_TAB, ModTabs::register);

    Constants.LOG.info("DisEnchantingTableFabric game objects initialized.");

    DisEnchantingTable.init();

    Constants.LOG.info("DisEnchantingTableFabric config initialized.");

    PayloadTypeRegistry.serverboundPlay().register(ModePacket.TYPE, ModePacket.STREAM_CODEC);
    ServerPlayNetworking.registerGlobalReceiver(ModePacket.TYPE, (payload, context) -> {
      if (!ModConfig.get().automaticModeAllowed) {
        return;
      }
      if (context.player().containerMenu instanceof DisEnchantingMenu menu
          && menu.getContainer() instanceof DisEnchantingTableTile tile) {
        tile.toggleMode();
      }
    });

    Constants.LOG.info("DisEnchantingTableFabric networking initialized.");

    DataResourceLoader.get().registerReloadListener(DisEnchantingTable.identifier(Constants.MOD_ID), new ResourceReloadListener());

    Constants.LOG.info("DisEnchantingTableFabric reload listener initialized.");
  }

  public <T> void bind(Registry<T> registry, Consumer<BiConsumer<T, Identifier>> source) {

    source.accept((t, rl) -> Registry.register(registry, rl, t));
  }

  public static class ResourceReloadListener extends SimplePreparableReloadListener<Void> {

    @Override
    public String getName() {
      return DisEnchantingTable.identifier(Constants.MOD_ID).toString();
    }

    @Override
    protected void apply(Void unused, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      ModConfig.load(Services.PLATFORM.getConfigDirectory());
    }

    @Override
    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      return null;
    }
  }
}
