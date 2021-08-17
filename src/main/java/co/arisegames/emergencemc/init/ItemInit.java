package co.arisegames.emergencemc.init;

import co.arisegames.emergencemc.EmergenceMC;
import co.arisegames.emergencemc.common.entities.EntityTypesInit;
import co.arisegames.emergencemc.common.items.Crowbar;
import co.arisegames.emergencemc.common.items.Extinguisher;
import co.arisegames.emergencemc.common.items.Flamethrower;
import co.arisegames.emergencemc.common.items.StretcherItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS;

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
                EmergenceMC.MOD_ID);

    }

    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item",
            () -> new Item(new Item.Properties().group(ItemGroup.MISC)));

    public static final RegistryObject<Item> EXTINGUISHER = ITEMS.register("extinguisher",
            Extinguisher::new);

    public static final RegistryObject<Item> FlAMETHROWER = ITEMS.register("flamethrower",
            Flamethrower::new);

    public static final RegistryObject<Item> CROWBAR = ITEMS.register("crowbar",
            Crowbar::new);

    public static final RegistryObject<Item> STRETCHER_ITEM = ITEMS.register("stretcher_item", StretcherItem::new);

}