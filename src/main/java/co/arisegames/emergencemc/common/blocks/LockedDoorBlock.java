package co.arisegames.emergencemc.common.blocks;

import net.minecraft.block.DoorBlock;

public class LockedDoorBlock extends DoorBlock {
    public LockedDoorBlock(Properties builder) {
        super(builder);
        setRegistryName("locked_door");
    }


}
