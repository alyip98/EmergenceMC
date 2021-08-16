package co.arisegames.emergencemc.common.items;

import co.arisegames.emergencemc.common.entities.EntityTypesInit;
import co.arisegames.emergencemc.common.entities.StretcherEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StretcherItem extends Item {
    public StretcherItem() {
        super(new Item.Properties().group(ItemGroup.MISC));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos base = context.getPos();
        if (!world.isRemote()) {
            StretcherEntity s = new StretcherEntity(EntityTypesInit.STRETCHER.get(), world);
            s.setPosition(base.getX(), base.getY() + 1, base.getZ());
            world.addEntity(s);
        }

        context.getItem().shrink(1);
        return ActionResultType.CONSUME;
    }
}
