package com.tfar.unstabletools;

import com.tfar.unstabletools.armor.ItemUnstableArmor;
import com.tfar.unstabletools.block.BlockUnstableBlock;
import com.tfar.unstabletools.item.ItemDivisionSign;
import com.tfar.unstabletools.item.ItemUnstableIngot;
import com.tfar.unstabletools.tools.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
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
  public static final ItemArmor.ArmorMaterial UNSTABLE_ARMOR = EnumHelper.addArmorMaterial(MODID + ":unstable_armor", MODID + ":unstable", Short.MAX_VALUE, new int[]{4, 7, 9, 4}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 5);
  public static final List<Item> MOD_ITEMS = new ArrayList<>();
  public static final List<Block> MOD_BLOCKS = new ArrayList<>();

  public static Logger logger;

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
    OreDictionary.registerOre("blockUnstable", ObjectHolders.unstableBlock);



  }

  @SubscribeEvent
  public static void colors(ColorHandlerEvent.Item e){
    ItemColors itemColors = e.getItemColors();
    itemColors.registerItemColorHandler(ObjectHolders.unstableIngot::getColor,(Item)ObjectHolders.unstableIngot);
    itemColors.registerItemColorHandler(ObjectHolders.divisionSign::getColor,(Item)ObjectHolders.divisionSign);

  }

  @SubscribeEvent
  public static void registerBlock(RegistryEvent.Register<Block> event) {
    IForgeRegistry<Block> registry = event.getRegistry();
    registerBlock(new BlockUnstableBlock(Material.IRON), "unstable_block", registry, 3000, 5);
  }

  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();

    registerItem(new ItemUnstableIngot(), "unstable_ingot", registry);
    registerItem(new ItemUnstableShears(), "unstable_shears", registry);

    registerItemBlock(ObjectHolders.unstableBlock, registry);

    registerItem(new ItemUnstableAxe(UNSTABLE), "unstable_axe", registry);
    registerItem(new ItemUnstableSpade(UNSTABLE), "unstable_spade", registry);
    registerItem(new ItemUnstablePickaxe(UNSTABLE), "unstable_pickaxe", registry);
    registerItem(new ItemUnstableSword(UNSTABLE), "unstable_sword", registry);
    registerItem(new ItemUnstableHoe(UNSTABLE), "unstable_hoe", registry);

    registerItem(new ItemUnstableArmor(UNSTABLE_ARMOR, EntityEquipmentSlot.HEAD), "unstable_helmet", registry);
    registerItem(new ItemUnstableArmor(UNSTABLE_ARMOR, EntityEquipmentSlot.CHEST), "unstable_chestplate", registry);
    registerItem(new ItemUnstableArmor(UNSTABLE_ARMOR, EntityEquipmentSlot.LEGS), "unstable_leggings", registry);
    registerItem(new ItemUnstableArmor(UNSTABLE_ARMOR, EntityEquipmentSlot.FEET), "unstable_boots", registry);

    ItemDivisionSign divisionSign = new ItemDivisionSign();
    divisionSign.setMaxStackSize(1);
    registerItem(divisionSign, "division_sign", registry);
  }

  private static void registerBlock(Block block, String name, IForgeRegistry<Block> registry, float blastResistance, float hardness) {
    block.setRegistryName(name);
    block.setTranslationKey(block.getRegistryName().toString());
    block.setCreativeTab(creativeTab);
    block.setResistance(blastResistance);
    block.setHardness(hardness);
    MOD_BLOCKS.add(block);
    registry.register(block);
  }

  private static void registerItem(Item item, String name, IForgeRegistry<Item> registry) {
    item.setRegistryName(name);
    item.setTranslationKey(item.getRegistryName().toString());
    item.setCreativeTab(creativeTab);
    MOD_ITEMS.add(item);
    registry.register(item);
  }

  private static void registerItemBlock(Block block, IForgeRegistry<Item> registry) {
    ItemBlock itemBlock = new ItemBlock(block);
    itemBlock.setRegistryName(block.getRegistryName());
    registry.register(itemBlock);
  }

  @SubscribeEvent
  public static void registerModels(ModelRegistryEvent event) {
    for (Item item : MOD_ITEMS)
      registerModelLocation(item);
    for (Block block : MOD_BLOCKS)
      registerModelLocation(Item.getItemFromBlock(block));
  }

  private static void registerModelLocation(Item item) {
    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
  }
  @SubscribeEvent
  public static void onEvent(LivingDropsEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    if (entity instanceof EntityWither && (event.getSource().getTrueSource() instanceof EntityPlayer) &&!(event.getSource().getTrueSource() instanceof FakePlayer)) {

      ItemStack itemStackToDrop = new ItemStack(ObjectHolders.divisionSign);
      event.getDrops().add(new EntityItem(entity.world, entity.posX,
              entity.posY, entity.posZ, itemStackToDrop));
    }
  }

}
