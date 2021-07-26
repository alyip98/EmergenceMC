package co.arisegames.emergencemc.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class Fire extends FireBlock {

    public Fire(Properties builder) {
        super(builder);
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        int base = super.getFlammability(state, world, pos, face);

        return 0;
    }
}
