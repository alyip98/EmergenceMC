package co.arisegames.emergencemc.common.network;

import co.arisegames.emergencemc.common.entities.StretcherEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;

import java.util.function.Supplier;

public class StretcherMovePacket {
    int playerID;
    float x;
    float y;

    public StretcherMovePacket(PacketBuffer buf) {
        this.playerID = buf.readInt();
        this.x = buf.readFloat();
        this.y = buf.readFloat();
    }

    public StretcherMovePacket(ClientPlayerEntity player, MovementInput movementInput) {
        this.playerID = player.getEntityId();
        Vector2f moveVector = movementInput.getMoveVector();
        this.x = moveVector.x;
        this.y = moveVector.y;
    }

    public StretcherMovePacket(ClientPlayerEntity player, Vector2f vec) {
        this.playerID = player.getEntityId();
        this.x = vec.x;
        this.y = vec.y;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.playerID);
        buf.writeFloat(this.x);
        buf.writeFloat(this.y);
    }

    public void process(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
//            LogManager.getLogger().info("Received packet: " + this.playerID);
            PlayerEntity player = ctx.get().getSender();
            if (player != null && player.isPassenger() && player.getRidingEntity() instanceof StretcherEntity)
                ((StretcherEntity) player.getRidingEntity()).updateInputs(player, new Vector2f(this.x, this.y));

        });
        ctx.get().setPacketHandled(true);
    }
}
