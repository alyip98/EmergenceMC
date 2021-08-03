package co.arisegames.emergencemc.data.client;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Function;

public class ModelProvider extends net.minecraftforge.client.model.generators.ModelProvider {
    public ModelProvider(DataGenerator generator, String modid, String folder, Function factory, ExistingFileHelper existingFileHelper) {
        super(generator, modid, folder, factory, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }

    @Override
    public String getName() {
        return null;
    }
}
