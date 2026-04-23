package io.github.jason13official.disenchanting_table.impl.common.util;

import net.minecraft.world.entity.player.Player;

public class ExperienceHelper {

  public static boolean hasEnoughExperiencePoints(Player player, int amount) {
    return player.isCreative() || amount <= 0 || getTotalPlayerExperiencePoints(player) >= amount;
  }

  public static boolean hasEnoughExperienceLevels(Player player, int amount) {
    return player.isCreative() || amount <= 0 || player.experienceLevel >= amount;
  }

  public static void deductExperiencePoints(Player player, int amount) {
    if (amount > 0) {
      player.giveExperiencePoints(-amount);
    }
  }

  public static void deductExperienceLevels(Player player, int amount) {
    if (amount > 0) {
      player.giveExperienceLevels(-amount);
    }
  }

  // Mirrors Player.getXpNeededForNextLevel() iteratively — no arithmetic series formula needed.
  public static int getTotalPlayerExperiencePoints(Player player) {
    return getTotalXpForLevel(player.experienceLevel) + (int) (player.experienceProgress * player.getXpNeededForNextLevel());
  }

  private static int getTotalXpForLevel(int level) {
    int total = 0;
    for (int i = 0; i < level; i++) {
      if (i >= 30) {
        total += 112 + (i - 30) * 9;
      } else if (i >= 15) {
        total += 37 + (i - 15) * 5;
      } else {
        total += 7 + i * 2;
      }
    }
    return total;
  }
}
