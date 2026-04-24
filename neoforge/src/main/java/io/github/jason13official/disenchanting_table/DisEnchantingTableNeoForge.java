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
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class DisEnchantingTableNeoForge {

  public static IEventBus EVENT_BUS;

  private static MinecraftServer server;

  public DisEnchantingTableNeoForge(final IEventBus modEventBus) {

    EVENT_BUS = modEventBus;

    bind(Registries.BLOCK, ModBlocks::register);
    bind(Registries.ENTITY_TYPE, ModEntities::register);
    bind(Registries.ITEM, ModItems::register);
    bind(Registries.PARTICLE_TYPE, ModParticles::register);
    bind(Registries.BLOCK_ENTITY_TYPE, ModTiles::register);
    bind(Registries.MENU, ModMenus::register);
    bind(Registries.CREATIVE_MODE_TAB, ModTabs::register);

    EVENT_BUS.addListener((Consumer<FMLCommonSetupEvent>) event -> {
      DisEnchantingTable.clientBoundPacketSender = PacketDistributor::sendToPlayer;
      DisEnchantingTable.init();
    });

    EVENT_BUS.addListener((Consumer<RegisterPayloadHandlersEvent>) event -> {
      PayloadRegistrar registrar = event.registrar(Constants.MOD_ID);
      registrar.playToServer(ModePacket.TYPE, ModePacket.STREAM_CODEC, (payload, context) -> {
        context.enqueueWork(() -> {
          if (!ModConfig.get().automaticModeAllowed) {
            return;
          }
          if (context.player().containerMenu instanceof DisEnchantingMenu menu
              && menu.getContainer() instanceof DisEnchantingTableTile tile) {
            tile.toggleMode();
          }
        });
      });
    });

    NeoForge.EVENT_BUS.addListener((Consumer<AddServerReloadListenersEvent>) event -> {
      event.addListener(DisEnchantingTable.identifier(Constants.MOD_ID), new ResourceReloadListener());
    });

    NeoForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {
      if (event.getEntity() instanceof ServerPlayer player) {
        DisEnchantingTable.sendConfigSyncPacketToClient(player);
      }
    });

    NeoForge.EVENT_BUS.addListener((Consumer<ServerStartedEvent>) event -> {
      server = event.getServer();
    });

    if (FMLLoader.getCurrent().getDist() == Dist.CLIENT) {
      new DisEnchantingTableClientNeoForge(EVENT_BUS);
    }
  }

  public <T> void bind(ResourceKey<Registry<T>> registryKey, Consumer<BiConsumer<T, Identifier>> source) {

    EVENT_BUS.addListener((Consumer<RegisterEvent>) event -> {
      if (registryKey.equals(event.getRegistryKey())) {
        source.accept((t, rl) -> event.register(registryKey, rl, () -> t));
      }
    });
  }

  public static class ResourceReloadListener extends SimplePreparableReloadListener<Void> {

    @Override
    public String getName() {
      return DisEnchantingTable.identifier(Constants.MOD_ID).toString();
    }

    @Override
    protected void apply(Void unused, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      ModConfig.load(Services.PLATFORM.getConfigDirectory());
      if (server != null) {
        server.getPlayerList().getPlayers().forEach(DisEnchantingTable::sendConfigSyncPacketToClient);
      }
    }

    @Override
    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
      return null;
    }
  }
}