package co.arisegames.emergencemc.common.items;

import co.arisegames.emergencemc.common.entities.EntityTypesInit;
import co.arisegames.emergencemc.common.entities.StretcherEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class StretcherItem extends Item {
    public StretcherItem() {
        super(new Item.Properties().group(ItemGroup.MISC));
    }

//    @Override
//    public ActionResultType onItemUse(ItemUseContext context) {
//        World world = context.getWorld();
//        BlockPos base = context.getPos();
//        if (!world.isRemote()) {
//            StretcherEntity s = new StretcherEntity(EntityTypesInit.STRETCHER.get(), world);
//            s.setPosition(base.getX(), base.getY() + 1, base.getZ());
//            world.addEntity(s);
//        }
//
//        context.getItem().shrink(1);
//        return ActionResultType.CONSUME;
//    }

//    @Override
//    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
//        World world = context.getWorld();
//        BlockPos base = context.getPos();
//        if (!world.isRemote()) {
//            StretcherEntity s = new StretcherEntity(EntityTypesInit.STRETCHER.get(), world);
//            s.setPosition(base.getX(), base.getY() + 1, base.getZ());
//            world.addEntity(s);
//        }
//
//        context.getItem().shrink(1);
//        return ActionResultType.CONSUME;
//    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerIn, Hand handIn) {
        playerIn.setActiveHand(handIn);
        Vector3d start = playerIn.getEyePosition(1);
        Vector3d look = playerIn.getLookVec();
        Vector3d end = start.add(look.scale(playerIn.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue()));
        BlockRayTraceResult result = world.rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY, playerIn));
        BlockPos base = result.getPos();
        if (!world.isRemote() && result.getType() == RayTraceResult.Type.BLOCK) {
            StretcherEntity s = new StretcherEntity(EntityTypesInit.STRETCHER.get(), world);
            s.setPosition(base.getX(), base.getY() + 1, base.getZ());
            world.addEntity(s);
        }

        playerIn.getHeldItem(handIn).shrink(1);

        return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
    }
}
