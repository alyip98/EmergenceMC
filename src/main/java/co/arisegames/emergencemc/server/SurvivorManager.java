package co.arisegames.emergencemc.server;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.world.IWorld;

import java.util.ArrayList;
import java.util.HashSet;

public class SurvivorManager <T extends LivingEntity> {
    private final HashSet<T> survivors = new HashSet<>();

    public SurvivorManager(IWorld world) {

    }

    public void addSurvivor(T survivor) {
        survivors.add(survivor);
    }

    public int getRemainingSurvivorCount() {
        return (int) survivors.stream().filter(t -> t != null && t.isAlive()).count();
    }
}
