package co.arisegames.emergencemc.common.items;

import co.arisegames.emergencemc.common.blocks.Fire;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Flamethrower extends Item {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int RADIUS = 2;
    private static final double RANGE = 5;

    public Flamethrower() {
        super(new Properties().maxDamage(100).group(ItemGroup.MISC));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        super.onUse(worldIn, livingEntityIn, stack, count);
//        LOGGER.info("use");
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        LOGGER.info("using");
        World world = player.getEntityWorld();
        Vector3d start = player.getEyePosition(1);
        Vector3d look = player.getLookVec();
        Vector3d end = start.add(look.scale(RANGE));
        BlockRayTraceResult result = world.rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, player));
        if (result.getType() != RayTraceResult.Type.MISS) {
            BlockPos base = result.getPos();
            for (int i = -RADIUS; i <= RADIUS; i++) {
                for (int j = -RADIUS; j <= RADIUS; j++) {
                    for (int k = -RADIUS; k <= RADIUS; k++) {
                        BlockPos target = base.add(i, j, k);
                        if (world.getBlockState(target).getBlock() == Blocks.AIR && world.getBlockState(target.down()).isSolid() && random.nextInt(3) == 0) {
                            world.setBlockState(target, Blocks.FIRE.getDefaultState());
                            for (int l = 0; l < 10; l++) {
                                world.addParticle(ParticleTypes.FLAME, target.getX(), target.getY(), target.getZ(), rand(), rand() + 1, rand());
                            }
                        }
                    }
                }
            }

            world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(base.add(-RADIUS, -RADIUS, -RADIUS), base.add(RADIUS, RADIUS, RADIUS))).forEach(
                    Entity::extinguish
            );
        }
        Vector3d vec1 = player.getPositionVec().add(0, 1.2, 0).add(look.scale(0.3));
        for (int i = 0; i < 5; i++) {
            Vector3d speed = look.scale(1).add(new Vector3d(rand(), rand(), rand()).scale(0.7));
            world.addParticle(ParticleTypes.CLOUD, vec1.x, vec1.y, vec1.z, speed.x, speed.y, speed.z);
        }
    }


    private double rand() {
        return random.nextDouble() - 0.5;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.setActiveHand(handIn);
        return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
    }
}
