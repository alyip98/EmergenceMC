package co.arisegames.emergencemc.common.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
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
            if (!world.isRemote) {
                swings++;
                if (random.nextDouble() < 0.03 || swings > 50) {
                    swings = 0;
                    door.openDoor(world, target, pos, true);
                    world.setBlockState(pos, world.getBlockState(pos));
                }
            }

            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 0.1f, random.nextFloat()/2 + 1.6f, false);

//            ((LockedDoorBlock) target.getBlock()).pry(context.getWorld(), target, pos);
            return ActionResultType.SUCCESS;
        }
        return super.onItemUse(context);
    }
}
