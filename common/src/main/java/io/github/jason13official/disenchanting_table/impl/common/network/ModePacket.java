package io.github.jason13official.disenchanting_table.impl.common.network;

import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ModePacket() implements CustomPacketPayload {

    public static final Type<ModePacket> TYPE = new Type<>(DisEnchantingTable.identifier("mode_toggle"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ModePacket> STREAM_CODEC =
        StreamCodec.unit(new ModePacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
