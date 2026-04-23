package io.github.jason13official.disenchanting_table.impl.common.block.tile;

import io.github.jason13official.disenchanting_table.impl.common.ModConfig;
import io.github.jason13official.disenchanting_table.impl.common.menu.DisEnchantingMenu;
import io.github.jason13official.disenchanting_table.impl.common.registry.ModTiles;
import io.github.jason13official.disenchanting_table.impl.common.util.ExperienceHelper;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class DisEnchantingTableTile extends AbstractDisEnchantingTile implements MenuProvider {

  public static final int DATA_PROGRESS = 0;
  public static final int DATA_MAX_PROGRESS = 1;
  public static final int DATA_MODE = 2;
  public static final int NUM_DATA_VALUES = 3;

  private static final int DEFAULT_MAX_PROGRESS = 100;

  private int progress;
  private int maxProgress = DEFAULT_MAX_PROGRESS;
  private DisenchantMode mode = DisenchantMode.MANUAL;

  private ItemStack progressInput = ItemStack.EMPTY;

  protected final ContainerData dataAccess = new ContainerData() {
    @Override
    public int get(int dataId) {
      return switch (dataId) {
        case DATA_PROGRESS -> DisEnchantingTableTile.this.progress;
        case DATA_MAX_PROGRESS -> DisEnchantingTableTile.this.maxProgress;
        case DATA_MODE -> DisEnchantingTableTile.this.mode.toInt();
        default -> 0;
      };
    }

    @Override
    public void set(int dataId, int value) {
      switch (dataId) {
        case DATA_PROGRESS -> DisEnchantingTableTile.this.progress = value;
        case DATA_MAX_PROGRESS -> DisEnchantingTableTile.this.maxProgress = value;
        case DATA_MODE -> DisEnchantingTableTile.this.mode = DisenchantMode.fromInt(value);
      }
    }

    @Override
    public int getCount() {
      return NUM_DATA_VALUES;
    }
  };

  public DisEnchantingTableTile(BlockPos worldPosition, BlockState blockState) {
    super(ModTiles.DISENCHANTING_TABLE, worldPosition, blockState);
  }

  public static void tickClient(Level level, BlockPos blockPos, BlockState state, DisEnchantingTableTile tile) {
  }

  public static void tickServer(Level level, BlockPos blockPos, BlockState state, DisEnchantingTableTile tile) {
    if (tile.mode != DisenchantMode.AUTO) return;

    tile.maxProgress = ModConfig.get().automaticDisenchantingTicks;

    ItemStack input = tile.getItem(0);

    if (!ItemStack.matches(input, tile.progressInput)) {
      tile.progress = 0;
      tile.progressInput = input.isEmpty() ? ItemStack.EMPTY : input.copy();
    }

    if (!tile.canDisenchant()) {
      tile.progress = 0;
      return;
    }

    if (tile.progress < tile.maxProgress) {
      tile.progress++;
      setChanged(level, blockPos, state);
      return;
    }

    Player nearby = level.getNearestPlayer(
        blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 8.0, false);
    if (nearby == null) return;

    int cost = computeXpCost();
    if (ModConfig.get().requiresExperience) {
      if (ModConfig.get().usesPoints) {
        if (!ExperienceHelper.hasEnoughExperiencePoints(nearby, cost)) return;
        ExperienceHelper.deductExperiencePoints(nearby, cost);
      } else {
        if (!ExperienceHelper.hasEnoughExperienceLevels(nearby, cost)) return;
        ExperienceHelper.deductExperienceLevels(nearby, cost);
      }
    }
    tile.setItem(2, tile.buildOutput(input));
    tile.playCompletionEffect();
    tile.consumeInputs();
    tile.progress = 0;
    tile.progressInput = ItemStack.EMPTY;
    tile.markUpdated();
  }

  public boolean canDisenchant() {
    ItemStack input = getItem(0);
    ItemStack extra = getItem(1);
    ItemStack output = getItem(2);
    if (input.isEmpty() || extra.isEmpty() || !output.isEmpty()) return false;
    if (!extra.is(Items.BOOK)) return false;
    return isValidInput(input);
  }

  public boolean isValidInput(ItemStack stack) {
    if (stack.is(Items.ENCHANTED_BOOK)) {
      return EnchantmentHelper.getEnchantmentsForCrafting(stack).size() >= 2;
    }
    return !EnchantmentHelper.getEnchantmentsForCrafting(stack).isEmpty();
  }

  public ItemStack buildOutput(ItemStack input) {
    ItemEnchantments enchants = EnchantmentHelper.getEnchantmentsForCrafting(input);
    if (enchants.isEmpty()) return ItemStack.EMPTY;

    if (input.is(Items.ENCHANTED_BOOK)) {
      Object2IntMap.Entry<Holder<Enchantment>> first = enchants.entrySet().iterator().next();
      ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
      mutable.set(first.getKey(), first.getIntValue());
      ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
      book.set(DataComponents.STORED_ENCHANTMENTS, mutable.toImmutable());
      return book;
    } else {
      ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
      book.set(DataComponents.STORED_ENCHANTMENTS, enchants);
      return book;
    }
  }

  public void consumeInputs() {
    ItemStack input = getItem(0);
    ItemEnchantments enchants = EnchantmentHelper.getEnchantmentsForCrafting(input);

    if (input.is(Items.ENCHANTED_BOOK)) {
      Object2IntMap.Entry<Holder<Enchantment>> first = enchants.entrySet().iterator().next();
      ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(enchants);
      mutable.set(first.getKey(), 0);
      ItemEnchantments remaining = mutable.toImmutable();
      if (remaining.isEmpty()) {
        setItem(0, new ItemStack(Items.BOOK));
      } else {
        ItemStack rebuilt = input.copy();
        rebuilt.set(DataComponents.STORED_ENCHANTMENTS, remaining);
        if (ModConfig.get().resetsRepairCost) rebuilt.set(DataComponents.REPAIR_COST, 0);
        setItem(0, rebuilt);
      }
    } else {
      ItemStack stripped = input.copy();
      stripped.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
      if (ModConfig.get().resetsRepairCost) stripped.set(DataComponents.REPAIR_COST, 0);
      setItem(0, stripped);
    }

    getItem(1).shrink(1);
  }

  public void playCompletionEffect() {
    if (this.level instanceof ServerLevel serverLevel) {
      double x = this.worldPosition.getX() + 0.5;
      double y = this.worldPosition.getY() + 1.0;
      double z = this.worldPosition.getZ() + 0.5;
      serverLevel.playSound(null, this.worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE,
          SoundSource.BLOCKS, 0.6F, serverLevel.getRandom().nextFloat() * 0.1F + 0.9F);
      serverLevel.sendParticles(ParticleTypes.ENCHANT, x, y, z, 8, 0.3, 0.2, 0.3, 0.1);
    }
  }

  public static int computeXpCost() {
    return ModConfig.get().experienceCost;
  }

  @Override
  public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
    if (this.mode != DisenchantMode.AUTO) return false;
    if (i == 0) return !isValidInput(itemStack);
    if (i == 2) return true;
    return false;
  }

  public void toggleMode() {
    if (this.mode == DisenchantMode.AUTO && !getItem(2).isEmpty()) return;
    this.mode = this.mode.next();
    this.progress = 0;
    this.progressInput = ItemStack.EMPTY;
    if (this.mode == DisenchantMode.AUTO) {
      setItem(2, ItemStack.EMPTY);
    }
    markUpdated();
  }

  public DisenchantMode getMode() {
    return mode;
  }

  public ItemStack getRenderedItemStack() {
    if (!getItem(2).isEmpty()) return getItem(2);
    if (!getItem(0).isEmpty()) return getItem(0);
    if (!getItem(1).isEmpty()) return new ItemStack(Items.BOOK);
    return ItemStack.EMPTY;
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    this.progress = input.getShortOr("Progress", (short) 0);
    this.maxProgress = input.getShortOr("MaxProgress", (short) DEFAULT_MAX_PROGRESS);
    this.mode = DisenchantMode.fromInt(input.getByteOr("Mode", (byte) 0));
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putShort("Progress", (short) this.progress);
    output.putShort("MaxProgress", (short) this.maxProgress);
    output.putByte("Mode", (byte) this.mode.toInt());
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("container.disenchanting_table");
  }

  @Override
  public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
    return new DisEnchantingMenu(i, inventory, this, this.dataAccess);
  }
}
