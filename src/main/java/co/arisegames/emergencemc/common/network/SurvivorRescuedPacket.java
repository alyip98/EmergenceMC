package co.arisegames.emergencemc.common.network;

import co.arisegames.emergencemc.util.RandomUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SurvivorRescuedPacket {
    float x;
    float y;
    float z;

    public SurvivorRescuedPacket(PacketBuffer buf) {
        this.x = buf.readFloat();
        this.y = buf.readFloat();
        this.z = buf.readFloat();
    }

    public SurvivorRescuedPacket(Vector3f vec) {
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = vec.getZ();
    }

    public SurvivorRescuedPacket(Vector3d vec) {
        this.x = (float) vec.getX();
        this.y = (float) vec.getY();
        this.z = (float) vec.getZ();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeFloat(this.x);
        buf.writeFloat(this.y);
        buf.writeFloat(this.z);
    }

    public void process(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> processClient(ctx));
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public void processClient(Supplier<NetworkEvent.Context> ctx) {
        World world = Minecraft.getInstance().world;
        Vector3d base = new Vector3d(this.x, this.y, this.z);
        if (world.isRemote) {
            for (int i = 0; i < 40; i++) {
                Vector3d p = RandomUtil.RandomPointInUnitCube().add(base).add(0, 0.5, 0);
                Vector3d vel = RandomUtil.RandomPointInUnitCube().scale(0.3).mul(1, 3, 1);
                world.addParticle(ParticleTypes.FIREWORK, p.getX(), p.getY(), p.getZ(), vel.getX(), vel.getY(), vel.getZ());
            }
        }
    }
}
