package co.arisegames.emergencemc.common.handlers;

import co.arisegames.emergencemc.EmergenceMC;
import co.arisegames.emergencemc.server.SurvivorManager;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldLoadHandler {
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        EmergenceMC.LOGGER.info("world load {}", event);
        IWorld world = event.getWorld();
        if (!world.isRemote()) {
            new SurvivorManager(world); // todo
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkDataEvent.Load event) {

//        EmergenceMC.LOGGER.info("world event {}", event);
        IWorld world = event.getWorld();
        IChunk chunk = event.getChunk();
        BlockPos corner = chunk.getPos().asBlockPos();
        world.getEntitiesWithinAABB(VillagerEntity.class, new AxisAlignedBB(corner, corner.add(16, 255, 16)));
        if (!world.isRemote()) {

        }
    }
}
