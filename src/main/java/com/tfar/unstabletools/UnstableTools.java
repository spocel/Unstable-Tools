package com.tfar.unstabletools;

import com.tfar.unstabletools.armor.ItemUnstableArmor;
import com.tfar.unstabletools.crafting.Config;
import com.tfar.unstabletools.crafting.RecipeDivision;
import com.tfar.unstabletools.item.ItemDivisionSign;
import com.tfar.unstabletools.item.ItemUnstableIngot;
import com.tfar.unstabletools.tools.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static com.tfar.unstabletools.UnstableTools.ObjectHolders.*;
import static com.tfar.unstabletools.UnstableTools.ObjectHolders.unstable_block;

@Mod(value = UnstableTools.MODID)
public class UnstableTools {

  public UnstableTools() {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
  }

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
      return Ingredient.fromItems(unstable_ingot);
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
      return Ingredient.fromItems(unstable_ingot);
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
  public static final Set<Item> MOD_ITEMS = new HashSet<>();

  public static ItemGroup creativeTab = new ItemGroup(MODID) {
    @Override
    public ItemStack createIcon() {
      return new ItemStack(unstable_pickaxe);
    }
  };

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event) {
      IForgeRegistry<Block> registry = event.getRegistry();
      registerObject(new Block(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 6000)) {

                       @Nonnull
                       @Override
                       public BlockRenderLayer getRenderLayer() {
                         return BlockRenderLayer.CUTOUT;
                       }

                       @Override
                       @OnlyIn(Dist.CLIENT)
                       public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
                         return adjacentBlockState.getBlock() == this
                                 || !adjacentBlockState.getBlock().isAir(adjacentBlockState, null, null);
                       }

                       @Override
                       public boolean isSolid(BlockState state) {
                         return true;
                       }

                       @Override
                       public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beacon) {
                         return true;
                       }
                     }
              , "unstable_block", registry);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
      IForgeRegistry<Item> registry = event.getRegistry();

      Item.Properties properties = new Item.Properties().group(creativeTab);

      registerObject(new ItemUnstableIngot(properties), "unstable_ingot", registry);
      registerObject(new ItemUnstableShears(properties), "unstable_shears", registry);

      registerObject(new BlockItem(unstable_block, properties), unstable_block.getRegistryName().getPath(), registry);

      registerObject(new ItemUnstableAxe(UNSTABLE, properties), "unstable_axe", registry);
      registerObject(new ItemUnstableSpade(UNSTABLE, 3, -1.5f, properties), "unstable_spade", registry);
      registerObject(new ItemUnstablePickaxe(UNSTABLE, 1, -2.8f, properties), "unstable_pickaxe", registry);
      registerObject(new ItemUnstableSword(UNSTABLE, 3, -2.4f, properties), "unstable_sword", registry);
      registerObject(new ItemUnstableHoe(UNSTABLE, 1, properties), "unstable_hoe", registry);

      registerObject(new ItemUnstableArmor(properties, UNSTABLE_ARMOR, EquipmentSlotType.HEAD), "unstable_helmet", registry);
      registerObject(new ItemUnstableArmor(properties, UNSTABLE_ARMOR, EquipmentSlotType.CHEST), "unstable_chestplate", registry);
      registerObject(new ItemUnstableArmor(properties, UNSTABLE_ARMOR, EquipmentSlotType.LEGS), "unstable_leggings", registry);
      registerObject(new ItemUnstableArmor(properties, UNSTABLE_ARMOR, EquipmentSlotType.FEET), "unstable_boots", registry);
      registerObject(new ItemDivisionSign(properties), "inactive_division_sign", registry);
      registerObject(new ItemDivisionSign(properties), "division_sign", registry);
      registerObject(new ItemDivisionSign(properties), "stable_division_sign", registry);
    }

    @SubscribeEvent
    public static void registerSerials(RegistryEvent.Register<IRecipeSerializer<?>> event) {
      registerObject(new SpecialRecipeSerializer<>(RecipeDivision::new),"division",event.getRegistry());
    }

    private static <T extends IForgeRegistryEntry<T>> void registerObject(T obj, String name, IForgeRegistry<T> registryObj) {
      registryObj.register(obj.setRegistryName(new ResourceLocation(MODID, name)));
      if (obj instanceof Item) MOD_ITEMS.add((Item) obj);
    }
  }

  @Mod.EventBusSubscriber(modid = MODID)
  public static class DropHandler {
    @SubscribeEvent
    public static void onEvent(LivingDropsEvent event) {
      LivingEntity entity = event.getEntityLiving();
      if (entity instanceof WitherEntity && (event.getSource().getTrueSource() instanceof PlayerEntity)
              && !(event.getSource().getTrueSource() instanceof FakePlayer)) {

        ItemStack itemStackToDrop = new ItemStack(inactive_division_sign);
        event.getDrops().add(new ItemEntity(entity.world, entity.posX,
                entity.posY, entity.posZ, itemStackToDrop));
      }
    }
  }

  @ObjectHolder(MODID)
  public static class ObjectHolders {

    public static final Item unstable_ingot = null;

    public static final Block unstable_block = null;

    public static final Item inactive_division_sign = null;

    public static final Item division_sign = null;

    public static final Item stable_division_sign = null;

    public static final Item unstable_pickaxe = null;

    public static final IRecipeSerializer<?> division = null;

  }
}
