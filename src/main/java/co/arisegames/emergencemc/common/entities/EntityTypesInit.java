package co.arisegames.emergencemc.common.entities;

import co.arisegames.emergencemc.EmergenceMC;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypesInit {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, EmergenceMC.MOD_ID);
    public static final RegistryObject<EntityType<StretcherEntity>> STRETCHER = ENTITY_TYPES.register("stretcher",
            () -> EntityType.Builder.create(StretcherEntity::new, EntityClassification.MISC).size(1f, 1.5f).build(new ResourceLocation(EmergenceMC.MOD_ID, "stretcher").toString()));
}
