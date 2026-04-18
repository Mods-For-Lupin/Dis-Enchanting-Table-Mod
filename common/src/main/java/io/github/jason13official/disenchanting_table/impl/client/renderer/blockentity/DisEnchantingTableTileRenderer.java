package io.github.jason13official.disenchanting_table.impl.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
    poseStack.translate(0.0F, 0.1F + Mth.sin(f * 0.1F) * 0.01F, 0.0F);

    this.crystalModel.setupAnim();
    VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entitySolid(CRYSTAL_TEXTURE));
    this.crystalModel.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);

    poseStack.popPose();
  }

  public static class CrystalModel extends Model {

    public static ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(DisEnchantingTable.identifier("crystal"), "main");

    private final ModelPart root;
    private final ModelPart crystal;

    public CrystalModel(ModelPart root) {
      super(RenderType::entitySolid);
      this.root = root;
      this.crystal = root.getChild("crystal");
    }

    public static LayerDefinition createBodyLayer() {
      MeshDefinition meshDefinition = new MeshDefinition();
      PartDefinition partDefinition = meshDefinition.getRoot();
      partDefinition.addOrReplaceChild("crystal", CubeListBuilder.create().texOffs(0, 0).addBox(0, 16.0625f, 0, 16.0f, 16.0f, 16.0f), PartPose.ZERO);
      return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setupAnim() {}
  }
}
