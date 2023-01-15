package tfar.unstabletools.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import tfar.unstabletools.datagen.data.ModBlockTagsProvider;
import tfar.unstabletools.datagen.data.ModItemTagsProvider;

public class Datagen {

    public static void gather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        boolean server = event.includeServer();
        generator.addProvider(server,new BlockConversionProvider(generator));
        BlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(generator,helper);
        generator.addProvider(server,blockTagsProvider);
        generator.addProvider(server,new ModItemTagsProvider(generator,blockTagsProvider,helper));
    }
}
