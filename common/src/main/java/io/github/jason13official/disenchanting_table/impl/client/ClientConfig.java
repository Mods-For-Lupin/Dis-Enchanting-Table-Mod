package io.github.jason13official.disenchanting_table.impl.client;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import io.github.jason13official.disenchanting_table.Constants;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClientConfig {

  private static ClientConfig INSTANCE = new ClientConfig();

  public boolean renderBlockParticles = true;
  public boolean renderExperienceCost = true;
  public boolean renderTableItem = true;

  public static ClientConfig get() {
    return INSTANCE;
  }

  public static void load(Path configDir) {
    Path file = configDir.resolve("disenchanting_table-client.toml");

    try {
      Files.createDirectories(configDir);
    } catch (Exception e) {
      Constants.LOG.error("Failed to create config directory, using defaults", e);
      return;
    }

    try (CommentedFileConfig config = CommentedFileConfig.builder(file.toFile()).build()) {
      if (Files.exists(file)) {
        config.load();
      }

      ClientConfig loaded = new ClientConfig();
      loaded.renderBlockParticles = config.getOrElse("render_block_particles", true);
      loaded.renderExperienceCost = config.getOrElse("render_experience_cost", true);
      loaded.renderTableItem = config.getOrElse("render_table_item", true);
      INSTANCE = loaded;

      config.setComment("render_block_particles", " Toggles particles spawned during disenchanting.");
      config.set("render_block_particles", INSTANCE.renderBlockParticles);
      config.setComment("render_experience_cost", " Toggles the 'Insufficient Experience' warning in the screen.");
      config.set("render_experience_cost", INSTANCE.renderExperienceCost);
      config.setComment("render_table_item", " Toggles the display of the current item on the table.");
      config.set("render_table_item", INSTANCE.renderTableItem);
      config.save();
    } catch (Exception e) {
      Constants.LOG.error("Failed to load Dis-Enchanting Table client config, using defaults", e);
      INSTANCE = new ClientConfig();
    }
  }
}
