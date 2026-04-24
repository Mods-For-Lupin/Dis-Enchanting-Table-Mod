package io.github.jason13official.disenchanting_table;

import io.github.jason13official.disenchanting_table.impl.common.ModConfig;
import io.github.jason13official.disenchanting_table.impl.common.network.ConfigSyncS2CPacket;
import io.github.jason13official.disenchanting_table.impl.common.network.ModePacket;
import io.github.jason13official.disenchanting_table.platform.Services;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

public class DisEnchantingTable {

  @Nullable
  public static BiConsumer<ServerPlayer, CustomPacketPayload> clientBoundPacketSender;

  public static void init() {
    ModConfig.load(Services.PLATFORM.getConfigDirectory());
  }

  public static Identifier identifier(final String path) {
    return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
  }

  public static void sendConfigSyncPacketToClient(ServerPlayer player) {
    if (clientBoundPacketSender != null) {
      ModConfig c = ModConfig.get();
      clientBoundPacketSender.accept(player, new ConfigSyncS2CPacket(c.automaticModeAllowed, c.requiresExperience, c.usesPoints, c.experienceCost, c.resetsRepairCost, c.automaticDisenchantingTicks));
    }
  }
}