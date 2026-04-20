package io.github.jason13official.disenchanting_table.impl.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisEnchantingTableTile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DisEnchantingTableTileRenderer implements BlockEntityRenderer<DisEnchantingTableTile> {

  private final ItemRenderer itemRenderer;

  public DisEnchantingTableTileRenderer(BlockEntityRendererProvider.Context context) {
    this.itemRenderer = context.getItemRenderer();
  }

  @Override
  public void render(DisEnchantingTableTile tile, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
    ItemStack output = tile.getItem(DisEnchantingTableTile.OUTPUT_SLOT);
    ItemStack input  = tile.getItem(DisEnchantingTableTile.INPUT_SLOT);
    ItemStack extra  = tile.getItem(DisEnchantingTableTile.EXTRA_SLOT);

    ItemStack toRender;
    if (!output.isEmpty()) {
      toRender = output;
    } else if (!input.isEmpty()) {
      toRender = input;
    } else if (!extra.isEmpty() && extra.getItem() instanceof BookItem) {
      toRender = new ItemStack(Items.ENCHANTED_BOOK);
    } else {
      return;
    }

    int seed = (int) tile.getBlockPos().asLong();

    poseStack.pushPose();
    poseStack.translate(0.5F, 0.77F, 0.5F);
    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
    poseStack.scale(0.5F, 0.5F, 0.5F);
    this.itemRenderer.renderStatic(toRender, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, tile.getLevel(), seed);
    poseStack.popPose();
  }
}
