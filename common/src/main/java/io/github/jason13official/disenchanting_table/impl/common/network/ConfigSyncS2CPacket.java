package io.github.jason13official.disenchanting_table.impl.common.network;

import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ConfigSyncS2CPacket(boolean automaticModeAllowed, boolean requiresExperience, boolean usesPoints, int experienceCost, boolean resetsRepairCost, int automaticDisenchantingTicks) implements CustomPacketPayload {

  public static final Type<ConfigSyncS2CPacket> TYPE = new Type<>(DisEnchantingTable.identifier("config_sync"));

  public static final StreamCodec<RegistryFriendlyByteBuf, ConfigSyncS2CPacket> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.BOOL, ConfigSyncS2CPacket::automaticModeAllowed,
          ByteBufCodecs.BOOL, ConfigSyncS2CPacket::requiresExperience,
          ByteBufCodecs.BOOL, ConfigSyncS2CPacket::usesPoints,
          ByteBufCodecs.VAR_INT, ConfigSyncS2CPacket::experienceCost,
          ByteBufCodecs.BOOL, ConfigSyncS2CPacket::resetsRepairCost,
          ByteBufCodecs.VAR_INT, ConfigSyncS2CPacket::automaticDisenchantingTicks,
          ConfigSyncS2CPacket::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
