package co.arisegames.emergencemc.common.blocks;

import co.arisegames.emergencemc.common.items.Crowbar;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.item.Item;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LockedDoorBlock extends DoorBlock {

    private static Logger LOGGER = LogManager.getLogger();
    public LockedDoorBlock(Properties builder) { // todo use a TileEntity
        super(builder);
        setRegistryName("locked_door");
    }

    @Override
    public void openDoor(World worldIn, BlockState state, BlockPos pos, boolean open) {
        super.openDoor(worldIn, state, pos, open);
    }
}
