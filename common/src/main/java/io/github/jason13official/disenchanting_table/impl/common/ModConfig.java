package io.github.jason13official.disenchanting_table.impl.common;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import io.github.jason13official.disenchanting_table.Constants;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {

  private static ModConfig INSTANCE = new ModConfig();

  public boolean requiresExperience = true;
  public boolean usesPoints = true;
  public int costMultiplier = 3;
  public int automaticDisenchantingTicks = 100;

  public static ModConfig get() {
    return INSTANCE;
  }

  public static void load(Path configDir) {
    Path file = configDir.resolve("disenchanting_table-server.toml");

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

      ModConfig loaded = new ModConfig();
      loaded.requiresExperience = config.getOrElse("requires_experience", true);
      loaded.usesPoints = config.getOrElse("uses_points", true);
      loaded.costMultiplier = config.getOrElse("cost_multiplier", 3);
      loaded.automaticDisenchantingTicks = config.getOrElse("automatic_disenchanting_ticks", 100);
      INSTANCE = loaded;

      config.setComment("requires_experience", " Whether the player must spend experience to disenchant.");
      config.set("requires_experience", INSTANCE.requiresExperience);
      config.setComment("uses_points", " If true, cost is in XP points; if false, cost is in XP levels.");
      config.set("uses_points", INSTANCE.usesPoints);
      config.setComment("cost_multiplier", " Multiplied by the sum of enchantment levels to determine XP cost.");
      config.set("cost_multiplier", INSTANCE.costMultiplier);
      config.setComment("automatic_disenchanting_ticks", " Number of ticks AUTO mode takes to complete a disenchant.");
      config.set("automatic_disenchanting_ticks", INSTANCE.automaticDisenchantingTicks);
      config.save();
    } catch (Exception e) {
      Constants.LOG.error("Failed to load Dis-Enchanting Table config, using defaults", e);
      INSTANCE = new ModConfig();
    }
  }
}
