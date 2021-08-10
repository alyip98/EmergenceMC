package co.arisegames.emergencemc.server;

import co.arisegames.emergencemc.EmergenceMC;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashSet;

public class SurvivorManager extends WorldSavedData {
    private final HashSet<VillagerEntity> survivors = new HashSet<>();
    public int totalSurvivorCount = 0;
    public int rescuedCount = 0;
    public int lossCount = 0;

    private static SurvivorManager instance;

    public static SurvivorManager get() {
        if (instance == null) instance = new SurvivorManager();
        return instance;
    }

    private SurvivorManager() {
        super("survivor_manager");
    }

    public void addSurvivor(VillagerEntity survivor) {
        totalSurvivorCount++;
        this.markDirty();
        survivors.add(survivor);
    }

    public int getRemainingSurvivorCount() {
        return (int) survivors.stream().filter(t -> t != null && t.isAlive()).count();
    }

    public void survivorRescued(VillagerEntity survivor) {
        rescuedCount++;
        this.markDirty();
        survivor.world.getPlayers().forEach(playerEntity -> playerEntity.sendMessage(new StringTextComponent("A survivor has been rescued! " + toString()), survivor.getUniqueID()));
    }


    public void survivorDied(VillagerEntity e) {
        if (!survivors.contains(e)) {
            return;
        }
        this.lossCount++;
        this.markDirty();
        e.world.getPlayers().forEach(playerEntity -> playerEntity.sendMessage(new StringTextComponent("A survivor has died! "+ toString()), e.getUniqueID()));
    }

    @Override
    public String toString() {
        return String.format("%d remaining (%d/%d/%d)", totalSurvivorCount - rescuedCount - lossCount, rescuedCount, lossCount, totalSurvivorCount);
    }

    @Override
    public void read(CompoundNBT nbt) {
        totalSurvivorCount = nbt.getInt("total_survivor_count");
        rescuedCount = nbt.getInt("rescued_survivor_count");
        lossCount = nbt.getInt("loss_survivor_count");
        EmergenceMC.LOGGER.info("loading data from nbt " + toString());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        EmergenceMC.LOGGER.info("writing data to nbt");
        compound.putInt("total_survivor_count", totalSurvivorCount);
        compound.putInt("rescued_survivor_count", rescuedCount);
        compound.putInt("loss_survivor_count", lossCount);
        return compound;
    }

    public void resetCounts() {
        EmergenceMC.LOGGER.info("resetting counts");
        totalSurvivorCount = 0;
        rescuedCount = 0;
        lossCount = 0;
    }
}
