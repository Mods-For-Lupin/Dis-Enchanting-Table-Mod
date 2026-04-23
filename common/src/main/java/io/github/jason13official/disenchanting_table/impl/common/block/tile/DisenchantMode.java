package io.github.jason13official.disenchanting_table.impl.common.block.tile;

public enum DisenchantMode {
  MANUAL, AUTO;

  public static DisenchantMode fromInt(int value) {
    return value == 1 ? AUTO : MANUAL;
  }

  public DisenchantMode next() {
    return this == MANUAL ? AUTO : MANUAL;
  }

  public int toInt() {
    return this == AUTO ? 1 : 0;
  }

  @Override
  public String toString() {
    return switch (this) {
      case AUTO -> "Auto";
      case MANUAL -> "Manual";
    };
  }
}
