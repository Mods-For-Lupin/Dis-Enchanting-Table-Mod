package io.github.jason13official.disenchanting_table.impl.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.jason13official.disenchanting_table.DisEnchantingTable;
import io.github.jason13official.disenchanting_table.impl.common.block.tile.DisenchantingTableTile;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DisEnchantingTableTileRenderer implements BlockEntityRenderer<DisenchantingTableTile> {

  public static final ResourceLocation CRYSTAL_TEXTURE = DisEnchantingTable.identifier("textures/entity/crystal.png");
  private final CrystalModel crystalModel;

  public DisEnchantingTableTileRenderer(BlockEntityRendererProvider.Context context) {
    this.crystalModel = new CrystalModel(context.bakeLayer(CrystalModel.LAYER_LOCATION));
  }

  @Override
  public void render(DisenchantingTableTile tile, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
    poseStack.pushPose();

    float f = (float)tile.time + partialTick;
    poseStack.translate(0.5F, 1.25F + Mth.sin(f * 0.1F) * 0.1F, 0.5F);
    poseStack.mulPose(Axis.YP.rotation(f * 0.05F));
    poseStack.scale(0.5F, 0.5F, 0.5F);

    this.crystalModel.setupAnim();
    VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entitySolid(CRYSTAL_TEXTURE));
    this.crystalModel.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);

    poseStack.popPose();
  }

  public static class CrystalModel extends Model {

    public static ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(DisEnchantingTable.identifier("crystal"), "main");

    private final ModelPart root;

    public CrystalModel(ModelPart root) {
      super(RenderType::entitySolid);
      this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
      MeshDefinition meshDefinition = new MeshDefinition();
      PartDefinition partDefinition = meshDefinition.getRoot();
      // Stepped Plumbob: tapers from 8-wide middle to points at top/bottom
      // UV layout calculated per-part to fit 64x32
      partDefinition.addOrReplaceChild("top_tip",
          CubeListBuilder.create().texOffs(0, 0).addBox(-1, 6, -1, 2, 2, 2), PartPose.ZERO);
      partDefinition.addOrReplaceChild("top",
          CubeListBuilder.create().texOffs(8, 0).addBox(-3, 3, -3, 6, 3, 6), PartPose.ZERO);
      partDefinition.addOrReplaceChild("mid_upper",
          CubeListBuilder.create().texOffs(0, 9).addBox(-4, 0, -4, 8, 3, 8), PartPose.ZERO);
      partDefinition.addOrReplaceChild("mid_lower",
          CubeListBuilder.create().texOffs(32, 9).addBox(-4, -3, -4, 8, 3, 8), PartPose.ZERO);
      partDefinition.addOrReplaceChild("bottom",
          CubeListBuilder.create().texOffs(0, 20).addBox(-3, -6, -3, 6, 3, 6), PartPose.ZERO);
      partDefinition.addOrReplaceChild("bottom_tip",
          CubeListBuilder.create().texOffs(24, 20).addBox(-1, -8, -1, 2, 2, 2), PartPose.ZERO);
      return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setupAnim() {}
  }
}
