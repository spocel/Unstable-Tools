package com.tfar.unstabletools;

import com.tfar.unstabletools.armor.ItemUnstableArmor;
import com.tfar.unstabletools.tools.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
@Mod(modid = UnstableTools.MODID, name = UnstableTools.NAME, version = UnstableTools.VERSION)
public class UnstableTools {
  public static final String MODID = "unstabletools";
  public static final String NAME = "Unstable Tools";
  public static final String VERSION = "1.0";

  public static final Item.ToolMaterial UNSTABLE = EnumHelper.addToolMaterial("UNSTABLE", 4, Short.MAX_VALUE, 8, 4, 25);
  public static final ItemArmor.ArmorMaterial UNSTABLE_ARMOR = EnumHelper.addArmorMaterial(MODID + ":unstable_armor", MODID + ":unstable", Short.MAX_VALUE, new int[]{3, 6, 8, 3}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 5);
  public static final List<Item> MOD_ITEMS = new ArrayList<>();

  private static Logger logger;

  public static CreativeTabs creativeTab = new CreativeTabs(MODID) {
    @Override
    public ItemStack createIcon() {
      return new ItemStack(ObjectHolders.unstablePickaxe);
    }
  };

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    UNSTABLE.setRepairItem(new ItemStack(ObjectHolders.unstableIngot));
    OreDictionary.registerOre("ingotUnstable", ObjectHolders.unstableIngot);
  }

  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();

    helper(new Item(), "unstable_ingot", registry);

    helper(new ItemUnstableAxe(UNSTABLE), "unstable_axe", registry);
    helper(new ItemUnstableSpade(UNSTABLE), "unstable_spade", registry);
    helper(new ItemUnstablePickaxe(UNSTABLE), "unstable_pickaxe", registry);
    helper(new ItemUnstableSword(UNSTABLE), "unstable_sword", registry);
    helper(new ItemUnstableHoe(UNSTABLE), "unstable_hoe", registry);


    helper(new ItemUnstableArmor(UNSTABLE_ARMOR, EntityEquipmentSlot.HEAD), "unstable_helmet", registry);
    helper(new ItemUnstableArmor(UNSTABLE_ARMOR, EntityEquipmentSlot.CHEST), "unstable_chestplate", registry);
    helper(new ItemUnstableArmor(UNSTABLE_ARMOR, EntityEquipmentSlot.LEGS), "unstable_leggings", registry);
    helper(new ItemUnstableArmor(UNSTABLE_ARMOR, EntityEquipmentSlot.FEET), "unstable_boots", registry);
  }

  private static void helper(Item item, String name, IForgeRegistry<Item> registry) {
    item.setRegistryName(name);
    item.setTranslationKey(item.getRegistryName().toString());
    item.setCreativeTab(creativeTab);
    MOD_ITEMS.add(item);
    registry.register(item);
  }

  @SubscribeEvent
  public static void registerModels(ModelRegistryEvent event) {
    for (Item item : MOD_ITEMS)
      helper2(item);
  }

  private static void helper2(Item item) {
    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
  }
}
