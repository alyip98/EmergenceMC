package co.arisegames.emergencemc.common.handlers;

import co.arisegames.emergencemc.common.entities.StretcherEntity;
import co.arisegames.emergencemc.common.network.StretcherMovePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MovementHandler {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onInput(InputUpdateEvent e) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator() || !player.isAlive() || player.movementInput == null)
            return;

        Vector2f raw = e.getMovementInput().getMoveVector();
        Vector3d look = player.getLookVec().mul(1, 0, 1).normalize();
        Vector3d move = new Vector3d(raw.x, 0, raw.y);
        double d1 = Math.PI/2 - Math.atan2(look.z, look.x);
        move = move.rotateYaw((float) d1);
        Vector2f finalVector = new Vector2f((float) move.x, (float) move.z);

        if (player.isPassenger() && player.getRidingEntity() instanceof StretcherEntity) {
            ((StretcherEntity) player.getRidingEntity()).updateInputs(player, finalVector);

            EmergenceMCPacketHandler.INSTANCE.sendToServer(new StretcherMovePacket(player, finalVector));
        }
    }
}
