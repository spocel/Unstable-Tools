package tfar.unstabletools.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import tfar.unstabletools.datagen.data.ModBlockTagsProvider;
import tfar.unstabletools.datagen.data.ModItemTagsProvider;

public class Datagen {

    public static void gather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        boolean server = event.includeServer();
        generator.addProvider(new BlockConversionProvider(generator));
        BlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(generator,helper);
        generator.addProvider(blockTagsProvider);
        generator.addProvider(new ModItemTagsProvider(generator,blockTagsProvider,helper));
    }
}
