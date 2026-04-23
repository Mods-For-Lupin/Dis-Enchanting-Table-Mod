package io.github.jason13official.disenchanting_table.impl.common;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import io.github.jason13official.disenchanting_table.Constants;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {

  private static ModConfig INSTANCE = new ModConfig();

  public boolean automaticModeAllowed = true;
  public boolean requiresExperience = true;
  public boolean usesPoints = true;
  public int experienceCost = 25;
  public boolean resetsRepairCost = true;
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
      loaded.automaticModeAllowed = config.getOrElse("automatic_mode_allowed", true);
      loaded.requiresExperience = config.getOrElse("requires_experience", true);
      loaded.usesPoints = config.getOrElse("uses_points", true);
      loaded.experienceCost = config.getOrElse("experience_cost", 25);
      loaded.resetsRepairCost = config.getOrElse("resets_repair_cost", true);
      loaded.automaticDisenchantingTicks = config.getOrElse("automatic_disenchanting_ticks", 100);
      INSTANCE = loaded;

      config.setComment("automatic_mode_allowed", " Whether players are allowed to use automatic mode.");
      config.set("automatic_mode_allowed", INSTANCE.automaticModeAllowed);
      config.setComment("requires_experience", " Whether the player must spend experience to disenchant.");
      config.set("requires_experience", INSTANCE.requiresExperience);
      config.setComment("uses_points", " If true, cost is in XP points; if false, cost is in XP levels.");
      config.set("uses_points", INSTANCE.usesPoints);
      config.setComment("experience_cost", " Flat XP cost (points or levels) per disenchant.");
      config.set("experience_cost", INSTANCE.experienceCost);
      config.setComment("resets_repair_cost", " Resets the item's anvil repair cost after disenchanting.");
      config.set("resets_repair_cost", INSTANCE.resetsRepairCost);
      config.setComment("automatic_disenchanting_ticks", " Number of ticks AUTO mode takes to complete a disenchant.");
      config.set("automatic_disenchanting_ticks", INSTANCE.automaticDisenchantingTicks);
      config.save();
    } catch (Exception e) {
      Constants.LOG.error("Failed to load Dis-Enchanting Table config, using defaults", e);
      INSTANCE = new ModConfig();
    }
  }
}
