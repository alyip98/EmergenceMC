package co.arisegames.emergencemc.common.entities;

import co.arisegames.emergencemc.common.entities.rendering.StretcherModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BoatModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;

//@OnlyIn(Dist.CLIENT)
//public class StretcherRenderer extends EntityRenderer<StretcherEntity> {
//    private final StretcherModel model = new StretcherModel();
//    public StretcherRenderer(EntityRendererManager renderManagerIn) {
//        super(renderManagerIn);
//        LogManager.getLogger().info("new stretcher renderer");
//    }
//
//    @Override
//    public ResourceLocation getEntityTexture(StretcherEntity entity) {
//        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
//    }
//
//
//    @Override
//    public void render(StretcherEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
//        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
//        model.render(matrixStackIn,
//                bufferIn.getBuffer(model.getRenderType(this.getEntityTexture(entityIn))),
//                super.getPackedLight(entityIn, 1),
//                OverlayTexture.NO_OVERLAY,
//                1,
//                1,
//                1,
//                1);
//    }
//}

@OnlyIn(Dist.CLIENT)
public class StretcherRenderer extends EntityRenderer<StretcherEntity> {
    private static final ResourceLocation BOAT_TEXTURE = new ResourceLocation("textures/entity/boat/oak.png");
    protected final BoatModel modelBoat = new BoatModel();

    public StretcherRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.8F;
    }

    public void render(StretcherEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0.0D, 0.375D, 0.0D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));

        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.modelBoat.getRenderType(this.getEntityTexture(entityIn)));
        this.modelBoat.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getEntityTexture(StretcherEntity entity) {
        return BOAT_TEXTURE;
    }
}
