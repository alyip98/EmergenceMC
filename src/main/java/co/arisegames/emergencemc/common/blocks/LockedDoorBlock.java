package co.arisegames.emergencemc.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LockedDoorBlock extends DoorBlock {
    private int swingCount = 0;
    public LockedDoorBlock(Properties builder) {
        super(builder);
        setRegistryName("locked_door");
    }

    @Override
    public void openDoor(World worldIn, BlockState state, BlockPos pos, boolean open) {
        super.openDoor(worldIn, state, pos, open);
    }
}
