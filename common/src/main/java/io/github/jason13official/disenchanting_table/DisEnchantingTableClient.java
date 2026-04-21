package io.github.jason13official.disenchanting_table;

import io.github.jason13official.disenchanting_table.impl.common.network.ModePacket;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.Nullable;

public class DisEnchantingTableClient {

  @Nullable
  public static Consumer<CustomPacketPayload> packetSender;

  public static void init() {
  }

  public static void sendModePacket(BlockPos pos) {
    if (packetSender != null) {
      packetSender.accept(new ModePacket(pos));
    }
  }
}
