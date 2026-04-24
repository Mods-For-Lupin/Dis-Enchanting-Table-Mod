package io.github.jason13official.disenchanting_table;

import io.github.jason13official.disenchanting_table.impl.client.ClientConfig;
import io.github.jason13official.disenchanting_table.impl.common.network.ModePacket;
import io.github.jason13official.disenchanting_table.platform.Services;
import java.util.function.Consumer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.Nullable;

public class DisEnchantingTableClient {

  @Nullable
  public static Consumer<CustomPacketPayload> serverBoundPacketSender;

  public static void init() {
    ClientConfig.load(Services.PLATFORM.getConfigDirectory());
  }

  public static void sendModePacketToServer() {
    if (serverBoundPacketSender != null) {
      serverBoundPacketSender.accept(new ModePacket());
    }
  }
}
