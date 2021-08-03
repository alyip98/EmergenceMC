package co.arisegames.emergencemc.client.models;

// Made with Blockbench 3.9.2
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class StretcherModel extends EntityModel<Entity> {
    private final ModelRenderer bb_main;

    public StretcherModel() {
        textureWidth = 128;
        textureHeight = 128;

        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(-7.0F, 23.5F, 17.5F);
        bb_main.setTextureOffset(45, 0).addBox(-7.0F, -0.5F, -13.5F, 14.0F, 1.0F, 27.0F, 0.0F, false);
        bb_main.setTextureOffset(0, 0).addBox(7.0F, -0.5F, -21.5F, 1.0F, 1.0F, 43.0F, 0.0F, false);
        bb_main.setTextureOffset(0, 44).addBox(-8.0F, -0.5F, -21.5F, 1.0F, 1.0F, 43.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        //previously the render function, render code was moved to a method below
    }

    public void setRotationAngles(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        bb_main.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }
}
