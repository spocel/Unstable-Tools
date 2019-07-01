package com.tfar.unstabletools;

import com.tfar.unstabletools.armor.ItemUnstableArmor;
import com.tfar.unstabletools.block.BlockUnstableBlock;
import com.tfar.unstabletools.crafting.RecipeDivision;
import com.tfar.unstabletools.item.ItemDivisionSign;
import com.tfar.unstabletools.item.ItemUnstableIngot;
import com.tfar.unstabletools.tools.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod(value = UnstableTools.MODID)
public class UnstableTools {
  public static final String MODID = "unstabletools";

  public static class UnstableTier implements IItemTier {

    @Override
    public int getMaxUses() {
      return 0;
    }

    @Override
    public float getEfficiency() {
      return 8;
    }

    @Override
    public float getAttackDamage() {
      return 8;
    }

    @Override
    public int getHarvestLevel() {
      return 4;
    }

    @Override
    public int getEnchantability() {
      return 25;
    }

    @Nonnull
    @Override
    public Ingredient getRepairMaterial() {
      return null;
    }
  }


  public static final IItemTier UNSTABLE = new UnstableTier();

  public static class UnstableArmorMaterial implements IArmorMaterial {

    private int[] array = new int[]{4, 7, 9, 4};

    @Override
    public int getDurability(@Nonnull EquipmentSlotType slotIn) {
      return 0;
    }

    @Override
    public int getDamageReductionAmount(@Nonnull EquipmentSlotType slotIn) {
      return array[slotIn.getIndex()];
    }

    @Override
    public int getEnchantability() {
      return 25;
    }

    @Nonnull
    @Override
    public SoundEvent getSoundEvent() {
      return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
    }

    @Nonnull
    @Override
    public Ingredient getRepairMaterial() {
      return Ingredient.fromItems(ObjectHolders.unstableIngot);
    }

    @Nonnull
    @Override
    public String getName() {
      return "unstable";
    }

    @Override
    public float getToughness() {
      return 5;
    }
  }


  public static final IArmorMaterial UNSTABLE_ARMOR = new UnstableArmorMaterial();
  public static final List<Item> MOD_ITEMS = new ArrayList<>();

  public static Logger logger;

  public static ItemGroup creativeTab = new ItemGroup(MODID) {
    @Override
    public ItemStack createIcon() {
      return new ItemStack(ObjectHolders.unstablePickaxe);
    }
  };

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    @SubscribeEvent
    public static void colors(ColorHandlerEvent.Item e) {
      ItemColors itemColors = e.getItemColors();
      itemColors.register(ObjectHolders.unstableIngot::getColor, (Item) ObjectHolders.unstableIngot);
      itemColors.register(ObjectHolders.divisionSign::getColor, (Item) ObjectHolders.divisionSign);

    }

    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event) {
      IForgeRegistry<Block> registry = event.getRegistry();
      registerBlock(new BlockUnstableBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(5,6000)), "unstable_block", registry);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
      IForgeRegistry<Item> registry = event.getRegistry();

      Item.Properties properties = new Item.Properties().group(creativeTab);

      registerItem(new ItemUnstableIngot(properties), "unstable_ingot", registry);
      registerItem(new ItemUnstableShears(properties), "unstable_shears", registry);

      registerItemBlock(ObjectHolders.unstableBlock,properties, registry);

      registerItem(new ItemUnstableAxe(UNSTABLE,properties), "unstable_axe", registry);
      registerItem(new ItemUnstableSpade(UNSTABLE, 3, -1.5f,properties), "unstable_spade", registry);
      registerItem(new ItemUnstablePickaxe(UNSTABLE, 1, -2.8f,properties), "unstable_pickaxe", registry);
      registerItem(new ItemUnstableSword(UNSTABLE,3,-2.4f,properties), "unstable_sword", registry);
      registerItem(new ItemUnstableHoe(UNSTABLE,1,properties), "unstable_hoe", registry);

      registerItem(new ItemUnstableArmor(properties, UNSTABLE_ARMOR, EquipmentSlotType.HEAD), "unstable_helmet",registry);
      registerItem(new ItemUnstableArmor(properties,UNSTABLE_ARMOR, EquipmentSlotType.CHEST), "unstable_chestplate",registry);
      registerItem(new ItemUnstableArmor(properties,UNSTABLE_ARMOR, EquipmentSlotType.LEGS), "unstable_leggings",registry);
      registerItem(new ItemUnstableArmor(properties,UNSTABLE_ARMOR, EquipmentSlotType.FEET), "unstable_boots",registry);
      registerItem(new ItemDivisionSign(properties), "division_sign",registry);
    }
    @SubscribeEvent
    public static void registerSerials(RegistryEvent.Register<IRecipeSerializer<?>> event) {

      IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();
      SpecialRecipeSerializer<RecipeDivision> obj = new SpecialRecipeSerializer<>(RecipeDivision::new);
      obj.setRegistryName("division");
      registry.register(obj);
    }

    private static void registerBlock(Block block,String name, IForgeRegistry<Block> registry) {
      registry.register(block.setRegistryName(name));
    }

    private static void registerItem(Item item, String name, IForgeRegistry<Item> registry) {
      item.setRegistryName(name);
      MOD_ITEMS.add(item);
      registry.register(item);
    }

    private static void registerItemBlock(Block block, Item.Properties properties,IForgeRegistry<Item> registry) {
      BlockItem itemBlock = new BlockItem(block,properties);
      itemBlock.setRegistryName(block.getRegistryName());
      registry.register(itemBlock);
    }
  }
  @SubscribeEvent
  public static void onEvent(LivingDropsEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity instanceof WitherEntity && (event.getSource().getTrueSource() instanceof PlayerEntity) && !(event.getSource().getTrueSource() instanceof FakePlayer)) {

      ItemStack itemStackToDrop = new ItemStack(ObjectHolders.divisionSign);
      event.getDrops().add(new ItemEntity(entity.world, entity.posX,
              entity.posY, entity.posZ, itemStackToDrop));
    }
  }

  @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
  public static class TooltipEvent {
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent e) {

      if(!Screen.hasControlDown())return;
      List<ITextComponent> tooltips = e.getToolTip();
      Item item = e.getItemStack().getItem();
      Map<ResourceLocation, Tag<Item>> tagmap = ItemTags.getCollection().getTagMap();
      for (Map.Entry<ResourceLocation, Tag<Item>> entry: tagmap.entrySet()){
        if (item.isIn(entry.getValue())){
          tooltips.add(new StringTextComponent(entry.getKey().toString()).applyTextStyle(TextFormatting.DARK_GRAY));
        }
      }
    }
  }

}
