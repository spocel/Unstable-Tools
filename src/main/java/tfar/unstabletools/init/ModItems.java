package tfar.unstabletools.init;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import tfar.unstabletools.item.DivisionSignItem;
import tfar.unstabletools.item.ItemUnstableIngot;
import tfar.unstabletools.item.StableDivisionSignItem;
import tfar.unstabletools.item.tools.ItemUnstableAxe;
import tfar.unstabletools.item.tools.ItemUnstableHoe;
import tfar.unstabletools.item.tools.ItemUnstableShears;
import tfar.unstabletools.item.tools.UnstableBowItem;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static tfar.unstabletools.UnstableTools.*;

public class ModItems {

    private static final Set<Item> ITEMS = new HashSet<>();
    static Item.Properties properties = new Item.Properties();
    public static final Item ethereal_glass = new BlockItem(ModBlocks.ethereal_glass, properties);
    public static final Item unstable_block = new BlockItem(ModBlocks.unstable_block, properties);
    public static final Item unstable_ingot = new ItemUnstableIngot(properties);
    public static final Item inactive_division_sign = new DivisionSignItem(properties);
    public static final Item division_sign = new DivisionSignItem(properties);
    public static final Item stable_division_sign = new StableDivisionSignItem(new Item.Properties().craftRemainder(ModItems.stable_division_sign));
    public static final Item unstable_axe = new ItemUnstableAxe(UNSTABLE, 9, -3, properties);
    public static final Item unstable_bow = new UnstableBowItem(properties);
    public static final Item unstable_fishing_rod = new FishingRodItem(properties);
    public static final Item unstable_pickaxe = new PickaxeItem(UNSTABLE, 1, -2.8f, properties);
    public static final Item unstable_hoe = new ItemUnstableHoe(UNSTABLE, -4, 0, properties);
    public static final Item unstable_shears = new ItemUnstableShears(properties);
    public static final Item unstable_shovel = new ShovelItem(UNSTABLE, 3, -1.5f, properties);
    public static final Item unstable_sword = new SwordItem(UNSTABLE, 3, -2.4f, properties);

    public static final Item unstable_helmet = new ArmorItem(UNSTABLE_ARMOR, EquipmentSlot.HEAD, properties);
    public static final Item unstable_chestplate = new ArmorItem(UNSTABLE_ARMOR, EquipmentSlot.CHEST, properties);
    public static final Item unstable_leggings = new ArmorItem(UNSTABLE_ARMOR, EquipmentSlot.LEGS, properties);
    public static final Item unstable_boots = new ArmorItem(UNSTABLE_ARMOR, EquipmentSlot.FEET, properties);

    public static Set<Item> getItems() {
        if (ITEMS.isEmpty()) {
            for (Field field : ModItems.class.getFields()) {
                try {
                    Object o = field.get(null);
                    if (o instanceof Item item) ITEMS.add(item);
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
            }
        }
        return ITEMS;
    }
}
