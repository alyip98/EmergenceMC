package co.arisegames.emergencemc.common.blocks;

import co.arisegames.emergencemc.init.BlockInit;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static co.arisegames.emergencemc.init.BlockInit.FIRE_BLOCK;

public class Fire extends FireBlock {
    private static final int ageRate = 20;
    private static final int tickInterval = 40;
    private static final Set<Block> protectedBlocks = new HashSet<>();
    private static final int encouragementBoost = 1;

    public Fire(AbstractBlock.Properties props) {
        super(props);
        protectedBlocks.add(Blocks.FIRE);
        protectedBlocks.add(Blocks.AIR);
        protectedBlocks.add(Blocks.DIRT);
        protectedBlocks.add(Blocks.GRASS_BLOCK);
        protectedBlocks.add(Blocks.STONE);
        protectedBlocks.add(Blocks.GRAVEL);
        protectedBlocks.add(Blocks.BEDROCK);
        protectedBlocks.add(Blocks.STRUCTURE_BLOCK);
        protectedBlocks.add(Blocks.STRUCTURE_VOID);
        protectedBlocks.add(Blocks.IRON_DOOR);
        protectedBlocks.add(BlockInit.RESCUE_BLOCK.get());
        protectedBlocks.add(this);
    }

    /**
     * Gets the delay before this block ticks again (without counting random ticks)
     */
    private static int getTickCooldown(Random rand) {
        return (tickInterval + rand.nextInt(tickInterval)) / 2;
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        worldIn.getPendingBlockTicks().scheduleTick(pos, this, getTickCooldown(worldIn.rand));
        if (worldIn.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
            if (!state.isValidPosition(worldIn, pos)) {
                worldIn.removeBlock(pos, false);
            }

            BlockState blockstate = worldIn.getBlockState(pos.down());
            boolean isFueled = blockstate.isFireSource(worldIn, pos, Direction.UP);
            int i = state.get(AGE);
            if (!isFueled && worldIn.isRaining() && this.canDie(worldIn, pos) && rand.nextFloat() < 0.2F + (float) i * 0.03F) {
                worldIn.removeBlock(pos, false);
            } else {
                int j = Math.min(15, i + rand.nextInt(ageRate + 1) / (ageRate));
                if (i != j) {
                    state = state.with(AGE, Integer.valueOf(j));
                    worldIn.setBlockState(pos, state, 4);
                }

                if (!isFueled && rand.nextInt(3) == 0) {
                    if (!this.areNeighborsFlammable(worldIn, pos)) {
                        BlockPos blockpos = pos.down();
                        if (!worldIn.getBlockState(blockpos).isSolidSide(worldIn, blockpos, Direction.UP) || i > 3) {
                            worldIn.removeBlock(pos, false);
                        }

                        return;
                    }

                    if (i == 15 && rand.nextInt(4) == 0 && !this.canCatchFire(worldIn, pos.down(), Direction.UP)) {
                        worldIn.removeBlock(pos, false);
                        return;
                    }
                }

                boolean isHumid = worldIn.isBlockinHighHumidity(pos);
                int k = isHumid ? -50 : 0;
                this.tryCatchFire(worldIn, pos.east(), 300 + k, rand, i, Direction.WEST);
                this.tryCatchFire(worldIn, pos.west(), 300 + k, rand, i, Direction.EAST);
                this.tryCatchFire(worldIn, pos.down(), 250 + k, rand, i, Direction.UP);
                this.tryCatchFire(worldIn, pos.up(), 250 + k, rand, i, Direction.DOWN);
                this.tryCatchFire(worldIn, pos.north(), 300 + k, rand, i, Direction.SOUTH);
                this.tryCatchFire(worldIn, pos.south(), 300 + k, rand, i, Direction.NORTH);
                BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

                for (int l = -1; l <= 1; ++l) {
                    for (int i1 = -1; i1 <= 1; ++i1) {
                        for (int j1 = -1; j1 <= 4; ++j1) {
                            if (l != 0 || j1 != 0 || i1 != 0) {
                                int k1 = 100;
                                if (j1 > 1) {
                                    k1 += (j1 - 1) * 100;
                                }

                                blockpos$mutable.setAndOffset(pos, l, j1, i1);
                                int l1 = this.getNeighborEncouragement(worldIn, blockpos$mutable);
                                if (l1 > 0) {
                                    int i2 = (l1 + 40 + worldIn.getDifficulty().getId() * 7) / (i + 30);
                                    if (isHumid) {
                                        i2 /= 2;
                                    }

                                    if (i2 > 0 && rand.nextInt(k1) <= i2 && (!worldIn.isRaining() || !this.canDie(worldIn, blockpos$mutable))) {
                                        int j2 = Math.min(15, i + rand.nextInt(5) / 4);
                                        worldIn.setBlockState(blockpos$mutable, this.getFireWithAge(worldIn, blockpos$mutable, j2), 3);
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private BlockState getFireWithAge(IWorld world, BlockPos pos, int age) {
        BlockState blockstate = getFireForPlacement(world, pos);
        return blockstate.matchesBlock(Blocks.FIRE) ? blockstate.with(AGE, age) : blockstate;
    }

    private int getNeighborEncouragement(IWorldReader worldIn, BlockPos pos) {
        if (!worldIn.isAirBlock(pos)) {
            return 0;
        } else {
            int i = 0;

            for (Direction direction : Direction.values()) {
                BlockState blockstate = worldIn.getBlockState(pos.offset(direction));
                i = Math.max(blockstate.getFireSpreadSpeed(worldIn, pos.offset(direction), direction.getOpposite()) + (isInvalidBlock(blockstate) ? 0 : encouragementBoost), i);
            }

            return i;
        }
    }

    private void tryCatchFire(World worldIn, BlockPos pos, int chance, Random random, int age, Direction face) {
        BlockState blockState = worldIn.getBlockState(pos);
        int i = blockState.getFlammability(worldIn, pos, face);
        if (isInvalidBlock(blockState.getBlock())) return;
        i += 10;
        if (blockState.getMaterial().isFlammable()) i += 15;
        if (random.nextInt(chance) < i) {
            BlockState blockstate = worldIn.getBlockState(pos);
            if (random.nextInt(age + 10) < 5 && !worldIn.isRainingAt(pos)) {
                int j = Math.min(age + random.nextInt(5) / 4, 15);
                worldIn.setBlockState(pos, this.getFireWithAge(worldIn, pos, j), 3);
            } else {
                worldIn.removeBlock(pos, false);
            }

            blockstate.catchFire(worldIn, pos, face, null);
        }

    }

    @Override
    public boolean canCatchFire(IBlockReader world, BlockPos pos, Direction face) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isFlammable(world, pos, face) || !isInvalidBlock(blockState.getBlock());
    }

    private boolean areNeighborsFlammable(IBlockReader worldIn, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (this.canCatchFire(worldIn, pos.offset(direction), direction.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    private boolean isInvalidBlock(Block block) {
        return protectedBlocks.contains(block);
    }

    private boolean isInvalidBlock(BlockState blockState) {
        return isInvalidBlock(blockState.getBlock());
    }


}
