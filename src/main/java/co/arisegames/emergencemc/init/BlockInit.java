package co.arisegames.emergencemc.init;

import co.arisegames.emergencemc.EmergenceMC;
import co.arisegames.emergencemc.common.blocks.RescueBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            EmergenceMC.MOD_ID);

    public static final RegistryObject<Block> EXAMPLE_BLOCK;
    private static final RegistryObject<RescueBlock> RESCUE_BLOCK;

    static {
        EXAMPLE_BLOCK = BLOCKS
                .register("example_block",
                        () -> new Block(AbstractBlock.Properties.create(Material.IRON, MaterialColor.GRAY)
                                .hardnessAndResistance(5f, 6f).harvestTool(ToolType.PICKAXE).harvestLevel(2)
                                .sound(SoundType.METAL)));
        RESCUE_BLOCK = BLOCKS.register("rescue_block", () -> new RescueBlock(AbstractBlock.Properties.create(Material.IRON, MaterialColor.GREEN).hardnessAndResistance(5f, 6f).harvestTool(ToolType.PICKAXE).harvestLevel(2)
                .sound(SoundType.METAL)));
    }
}
