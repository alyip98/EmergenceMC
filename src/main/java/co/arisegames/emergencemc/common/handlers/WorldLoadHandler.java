package co.arisegames.emergencemc.common.handlers;

import co.arisegames.emergencemc.EmergenceMC;
import co.arisegames.emergencemc.server.SurvivorManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldLoadHandler {
    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        SurvivorManager.get().resetCounts();
    }

    @SubscribeEvent
    public static void onEntityConstructon(EntityEvent.EntityConstructing event) {
        Entity e = event.getEntity();
        if (e instanceof VillagerEntity && !e.world.isRemote) {
            EmergenceMC.LOGGER.info(e);
            SurvivorManager.get().addSurvivor(((VillagerEntity) e));
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        Entity e = event.getEntity();
        if (e instanceof VillagerEntity && !e.world.isRemote) {
            EmergenceMC.LOGGER.info(e);
            SurvivorManager.get().survivorDied(((VillagerEntity) e));
        }
    }
}
