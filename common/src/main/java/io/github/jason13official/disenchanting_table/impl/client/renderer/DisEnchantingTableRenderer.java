package io.github.jason13official.disenchanting_table.impl.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisEnchantingTableTile;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class DisEnchantingTableRenderer implements BlockEntityRenderer<DisEnchantingTableTile, DisEnchantingTableRenderer.RenderState> {

  private final ItemModelResolver itemModelResolver;

  public DisEnchantingTableRenderer(BlockEntityRendererProvider.Context ctx) {
    this.itemModelResolver = ctx.itemModelResolver();
  }

  @Override
  public RenderState createRenderState() {
    return new RenderState();
  }

  @Override
  public void extractRenderState(DisEnchantingTableTile tile, RenderState state, float f, Vec3 vec3, ModelFeatureRenderer.@Nullable CrumblingOverlay overlay) {
    BlockEntityRenderer.super.extractRenderState(tile, state, f, vec3, overlay);
    state.facing = tile.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
    state.item = new ItemStackRenderState();
    this.itemModelResolver.updateForTopItem(state.item, tile.getRenderedItemStack(), ItemDisplayContext.FIXED, tile.getLevel(), null, (int) tile.getBlockPos().asLong());
  }


  @Override
  public void submit(RenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
    if (state.item == null || state.item.isEmpty()) {
      return;
    }

    poseStack.pushPose();
    poseStack.translate(0.5F, 0.75F, 0.5F);
    poseStack.scale(0.5F, 0.5F, 0.5F);
    Direction facing = Direction.from2DDataValue(state.facing.get2DDataValue() % 4);
    poseStack.mulPose(Axis.YP.rotationDegrees(facing != Direction.EAST && facing != Direction.WEST ? facing.getOpposite().toYRot() : facing.toYRot()));
    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
    state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    poseStack.popPose();
  }

  @Override
  public boolean shouldRender(DisEnchantingTableTile tile, Vec3 cameraPos) {
    return true;
  }

  public static class RenderState extends BlockEntityRenderState {

    public ItemStackRenderState item;
    public Direction facing = Direction.NORTH;
  }
}
