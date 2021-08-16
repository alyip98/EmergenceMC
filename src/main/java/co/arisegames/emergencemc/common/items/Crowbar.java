package co.arisegames.emergencemc.common.items;

import co.arisegames.emergencemc.common.blocks.LockedDoorBlock;
import co.arisegames.emergencemc.common.entities.EntityTypesInit;
import co.arisegames.emergencemc.common.entities.StretcherEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
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

    @Override
    public int getUseDuration(ItemStack stack) {
        return 30;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        BlockPos pos = context.getPos();
        BlockState target = context.getWorld().getBlockState(pos);

        World world = context.getWorld();
        if (target.getBlock() instanceof DoorBlock) {
            DoorBlock door = (DoorBlock) target.getBlock();
            if (!world.isRemote && random.nextDouble() < 0.1) {
                door.openDoor(world, target, pos, true);
                world.setBlockState(pos, world.getBlockState(pos));
            }

            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 0.1f, random.nextFloat()/2 + 1.6f, false);

//            ((LockedDoorBlock) target.getBlock()).pry(context.getWorld(), target, pos);
            return ActionResultType.SUCCESS;
        }
        return super.onItemUse(context);
    }
}
