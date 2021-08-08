package co.arisegames.emergencemc.common.items;

import co.arisegames.emergencemc.common.entities.EntityTypesInit;
import co.arisegames.emergencemc.common.entities.StretcherEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Crowbar extends Item {

    private static final Logger LOGGER = LogManager.getLogger();

    public Crowbar() {
        super(new Item.Properties().maxDamage(100).group(ItemGroup.MISC));
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        LOGGER.info("swing");
        return false;
    }
}
