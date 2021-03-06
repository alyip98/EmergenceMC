package co.arisegames.emergencemc.client.render;

import co.arisegames.emergencemc.EmergenceMC;
import co.arisegames.emergencemc.client.models.StretcherModel;
import co.arisegames.emergencemc.common.entities.StretcherEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class StretcherRenderer extends EntityRenderer<StretcherEntity> {
    private static final ResourceLocation BOAT_TEXTURE = new ResourceLocation("textures/entity/boat/oak.png");
    protected final StretcherModel model = new StretcherModel();

    public StretcherRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.8F;
    }

    public void render(StretcherEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        int count = entityIn.getPlayerPassengers();
        if (count == 0) {
//            matrixStackIn.translate(0, -1.4375f, 0);
        } else if (count == 1) {
//            Vector3f.YP
            Vector3d axis = entityIn.getForward();
//            matrixStackIn.rotate(new Quaternion(new Vector3f((float) axis.x, (float) axis.y, (float) axis.z), 45, true));
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-15));
            matrixStackIn.translate(0, 0.375, 0);
        } else {
            matrixStackIn.translate(0, 0.5, 0);
        }
//        matrixStackIn.translate(0, -0.5, 0);

////
//        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
//        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.getRenderType(this.getEntityTexture(entityIn)));
        this.model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getEntityTexture(StretcherEntity entity) {
        return new ResourceLocation(EmergenceMC.MOD_ID, "textures/entities/stretcher/stretcher.png");
    }
}
