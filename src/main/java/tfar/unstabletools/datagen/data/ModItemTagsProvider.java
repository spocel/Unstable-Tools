package tfar.unstabletools.datagen.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tfar.unstabletools.UnstableTools;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator p_126511_,BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126511_,blockTagsProvider, UnstableTools.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {

    }
}
