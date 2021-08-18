package co.arisegames.emergencemc.common.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Crowbar extends Item {

    private static final Logger LOGGER = LogManager.getLogger();
    private int swings = 0;

    public Crowbar() {
        super(new Item.Properties().maxDamage(100).group(ItemGroup.MISC));
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
//        LOGGER.info("swing");
        return false;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 30;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {


//            ((LockedDoorBlock) target.getBlock()).pry(context.getWorld(), target, pos);

        return super.onItemUse(context);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        World world = playerIn.world;

        Vector3d start = playerIn.getEyePosition(1);
        Vector3d look = playerIn.getLookVec();
        Vector3d end = start.add(look.scale(playerIn.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue()));
        BlockRayTraceResult result = world.rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, playerIn));
        BlockPos pos = result.getPos();
        BlockState target = world.getBlockState(pos);

        if (target.getBlock() instanceof DoorBlock) {
            DoorBlock door = (DoorBlock) target.getBlock();
            if (!world.isRemote) {
                swings++;
                if (random.nextDouble() < 0.03 || swings > 50) {
                    swings = 0;
                    door.openDoor(world, target, pos, true);
                    world.setBlockState(pos, world.getBlockState(pos));
                }
            }

            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 0.1f, random.nextFloat() / 2 + 1.6f, false);
            return ActionResult.resultConsume(playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);

    }
}
