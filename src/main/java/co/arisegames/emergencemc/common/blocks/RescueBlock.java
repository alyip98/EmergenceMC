package co.arisegames.emergencemc.common.blocks;

import co.arisegames.emergencemc.common.handlers.EmergenceMCPacketHandler;
import co.arisegames.emergencemc.common.network.SurvivorRescuedPacket;
import co.arisegames.emergencemc.util.Util;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class RescueBlock extends Block {
    public RescueBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityWalk(worldIn, pos, entityIn);

        if (entityIn instanceof VillagerEntity) {
//            LOGGER.info("villager on top");
//            RemoveVillager((VillagerEntity) entityIn);
            entityIn.remove();
            worldIn.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1f, 1f);
            if (!worldIn.isRemote)
                EmergenceMCPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new SurvivorRescuedPacket(entityIn.getPositionVec()));
            worldIn.getPlayers().forEach(playerEntity -> {
                playerEntity.sendMessage(new StringTextComponent("A survivor has been rescued!"), entityIn.getUniqueID());
            });
        }
    }
}
