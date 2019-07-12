package com.tfar.unstabletools;

import com.tfar.unstabletools.armor.ItemUnstableArmor;
import com.tfar.unstabletools.block.BlockUnstableBlock;
import com.tfar.unstabletools.crafting.RecipeDivision;
import com.tfar.unstabletools.item.ItemDivisionSign;
import com.tfar.unstabletools.item.ItemUnstableIngot;
import com.tfar.unstabletools.tools.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.tfar.unstabletools.UnstableTools.ObjectHolders.unstable_block;

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
      return Ingredient.fromItems(ObjectHolders.unstable_ingot);
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
      return Ingredient.fromItems(ObjectHolders.unstable_ingot);
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
      return new ItemStack(ObjectHolders.unstable_pickaxe);
    }
  };

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event) {
      IForgeRegistry<Block> registry = event.getRegistry();
      registerBlock(new BlockUnstableBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 6000)), "unstable_block", registry);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
      IForgeRegistry<Item> registry = event.getRegistry();

      Item.Properties properties = new Item.Properties().group(creativeTab);

      registerItem(new ItemUnstableIngot(properties), "unstable_ingot", registry);
      registerItem(new ItemUnstableShears(properties), "unstable_shears", registry);

      registerItem(new BlockItem(unstable_block, properties), unstable_block.getRegistryName().toString(), registry);

      registerItem(new ItemUnstableAxe(UNSTABLE, properties), "unstable_axe", registry);
      registerItem(new ItemUnstableSpade(UNSTABLE, 3, -1.5f, properties), "unstable_spade", registry);
      registerItem(new ItemUnstablePickaxe(UNSTABLE, 1, -2.8f, properties), "unstable_pickaxe", registry);
      registerItem(new ItemUnstableSword(UNSTABLE, 3, -2.4f, properties), "unstable_sword", registry);
      registerItem(new ItemUnstableHoe(UNSTABLE, 1, properties), "unstable_hoe", registry);

      registerItem(new ItemUnstableArmor(properties, UNSTABLE_ARMOR, EquipmentSlotType.HEAD), "unstable_helmet", registry);
      registerItem(new ItemUnstableArmor(properties, UNSTABLE_ARMOR, EquipmentSlotType.CHEST), "unstable_chestplate", registry);
      registerItem(new ItemUnstableArmor(properties, UNSTABLE_ARMOR, EquipmentSlotType.LEGS), "unstable_leggings", registry);
      registerItem(new ItemUnstableArmor(properties, UNSTABLE_ARMOR, EquipmentSlotType.FEET), "unstable_boots", registry);
      registerItem(new ItemDivisionSign(properties), "division_sign", registry);
    }

    @SubscribeEvent
    public static void registerSerials(RegistryEvent.Register<IRecipeSerializer<?>> event) {

      IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();
      SpecialRecipeSerializer<RecipeDivision> obj = new SpecialRecipeSerializer<>(RecipeDivision::new);
      obj.setRegistryName("division");
      registry.register(obj);
    }

    private static void registerBlock(Block block, String name, IForgeRegistry<Block> registry) {
      registry.register(block.setRegistryName(name));
    }

    private static void registerItem(Item item, String name, IForgeRegistry<Item> registry) {
      item.setRegistryName(name);
      MOD_ITEMS.add(item);
      registry.register(item);
    }
  }

  @SubscribeEvent
  public static void onEvent(LivingDropsEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity instanceof WitherEntity && (event.getSource().getTrueSource() instanceof PlayerEntity) && !(event.getSource().getTrueSource() instanceof FakePlayer)) {

      ItemStack itemStackToDrop = new ItemStack(ObjectHolders.division_sign);
      event.getDrops().add(new ItemEntity(entity.world, entity.posX,
              entity.posY, entity.posZ, itemStackToDrop));
    }
  }

  @ObjectHolder(MODID)
  public static class ObjectHolders {

    public static final Item unstable_ingot = null;

    public static final Block unstable_block = null;

    public static final Item division_sign = null;

    public static final Item unstable_pickaxe = null;

    public static final IRecipeSerializer<?> compression = null;

  }
}
