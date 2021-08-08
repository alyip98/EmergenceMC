package co.arisegames.emergencemc;

import co.arisegames.emergencemc.common.blocks.LockedDoorBlock;
import co.arisegames.emergencemc.common.entities.EntityTypesInit;
import co.arisegames.emergencemc.client.render.StretcherRenderer;
import co.arisegames.emergencemc.common.handlers.EmergenceMCPacketHandler;
import co.arisegames.emergencemc.common.items.Crowbar;
import co.arisegames.emergencemc.common.items.Extinguisher;
import co.arisegames.emergencemc.common.network.StretcherMovePacket;
import co.arisegames.emergencemc.common.network.SurvivorRescuedPacket;
import co.arisegames.emergencemc.init.BlockInit;
import co.arisegames.emergencemc.init.ItemInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("emergencemc")
public class EmergenceMC
{
    public static final String MOD_ID = "emergencemc";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    private static int messageID = 0;

    public EmergenceMC() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BlockInit.BLOCKS.register(bus);
        ItemInit.ITEMS.register(bus);
        EntityTypesInit.ENTITY_TYPES.register(bus);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        EmergenceMCPacketHandler.INSTANCE.registerMessage(messageID++, StretcherMovePacket.class, StretcherMovePacket::toBytes, StretcherMovePacket::new, StretcherMovePacket::process);
        EmergenceMCPacketHandler.INSTANCE.registerMessage(messageID++, SurvivorRescuedPacket.class, SurvivorRescuedPacket::toBytes, SurvivorRescuedPacket::new, SurvivorRescuedPacket::process);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);

        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.STRETCHER.get(), StretcherRenderer::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
            blockRegistryEvent.getRegistry().register(new LockedDoorBlock(AbstractBlock.Properties.create(Material.IRON)));
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            LOGGER.info("Registering items");

            BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
                itemRegistryEvent.getRegistry().register(new BlockItem(block, new Item.Properties().group(ItemGroup.MISC))
                        .setRegistryName(block.getRegistryName()));
            });
//            itemRegistryEvent.getRegistry().registerAll(new BlockItem(new LockedDoorBlock(AbstractBlock.Properties.create(Material.IRON)), new Item.Properties()));
        }
    }
}
