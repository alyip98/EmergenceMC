package co.arisegames.emergencemc.common.handlers;

import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityHurt (LivingHurtEvent event) {
        event.setAmount(event.getAmount() * getDamageModifier(event.getSource()));

        if (event.getSource() == DamageSource.IN_WALL) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityAttack (LivingAttackEvent event) {
        if (event.getSource() == DamageSource.IN_WALL) {
            event.setCanceled(true);
        }
    }

    private static float getDamageModifier(DamageSource source) {
        return source == DamageSource.IN_WALL ? 0 : 1;
    }
}
