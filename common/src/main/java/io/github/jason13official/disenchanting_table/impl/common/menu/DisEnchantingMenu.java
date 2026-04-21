package io.github.jason13official.disenchanting_table.impl.common.menu;

import io.github.jason13official.disenchanting_table.impl.common.registry.ModMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class DisEnchantingMenu extends AbstractContainerMenu {

  public DisEnchantingMenu(int containerId, Inventory inventory) {
    this(containerId, inventory, new SimpleContainer(3), new SimpleContainerData(1));
  }

  public DisEnchantingMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
    super(ModMenus.DISENCHANTING_TABLE, containerId);
  }

  @Override
  public ItemStack quickMoveStack(Player player, int i) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean stillValid(Player player) {
    return !player.isDeadOrDying();
  }
}
