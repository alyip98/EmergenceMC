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
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Extinguisher extends Item {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int RADIUS = 3;
    private static final double RANGE = 5;

    public Extinguisher() {
        super(new Item.Properties().maxDamage(100).group(ItemGroup.MISC));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 16;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        super.onUse(worldIn, livingEntityIn, stack, count);
        LOGGER.info("use");
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        LOGGER.info("using");
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        LOGGER.info("first");
        return ActionResultType.PASS;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        LOGGER.info("finish");
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        BlockPos base = context.getPos();
        World world = context.getWorld();

        if (!world.isRemote()) {
            StretcherEntity s = new StretcherEntity(EntityTypesInit.STRETCHER.get(), world);
            s.setPosition(base.getX(), base.getY() + 1, base.getZ());
            world.addEntity(s);
        }


        LOGGER.info("Target: {}", base);

        for (int i = -RADIUS; i <= RADIUS; i++) {
            for (int j = -RADIUS; j <= RADIUS; j++) {
                for (int k = -RADIUS; k <= RADIUS; k++) {
                    BlockPos target = base.add(i, j, k);
                    if (world.getBlockState(target).getBlock() == Blocks.FIRE) {
                        world.destroyBlock(target, true);
                        for (int l = 0; l < 10; l++) {
                            world.addParticle(ParticleTypes.SMOKE, target.getX(), target.getY(), target.getZ(), rand(), rand() + 1, rand());
                        }
                    }
                }
            }
        }


        return ActionResultType.PASS;
    }

    private double rand() {
        return random.nextDouble() - 0.5;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        LOGGER.info("stop using");
        super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        LOGGER.info("right click");
        playerIn.getLookVec();
        Vector3d start = playerIn.getEyePosition(1);
        Vector3d look = playerIn.getLookVec();
        Vector3d end = start.add(look.scale(RANGE));
        BlockRayTraceResult result = worldIn.rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, playerIn));
        BlockPos target = result.getPos();
        Vector3d vec1 = playerIn.getPositionVec().add(0, 1.5, 0).add(look.scale(0.2));
        for (int i = 0; i < 10; i++) {
            Vector3d speed = look.scale(0.3).add(new Vector3d(rand(), rand(), rand()).scale(0.1));
            worldIn.addParticle(ParticleTypes.CLOUD, vec1.x, vec1.y, vec1.z, speed.x, speed.y, speed.z);
        }
        if (result.getType() == RayTraceResult.Type.MISS) {
            target = result.getPos();
        }
//        worldIn.destroyBlock(target, true);
        LOGGER.info(result);
        return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
    }
}
