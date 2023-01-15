package tfar.unstabletools;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import tfar.unstabletools.crafting.Config;
import tfar.unstabletools.crafting.ConversionManager;
import tfar.unstabletools.crafting.RecipeDivision;
import tfar.unstabletools.datagen.Datagen;
import tfar.unstabletools.item.DivisionSignItem;
import tfar.unstabletools.item.ItemUnstableIngot;
import tfar.unstabletools.tools.ItemUnstableAxe;
import tfar.unstabletools.tools.ItemUnstableHoe;
import tfar.unstabletools.tools.ItemUnstableShears;
import tfar.unstabletools.tools.UnstableBowItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;
import static tfar.unstabletools.UnstableTools.ObjectHolders.*;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

@Mod(value = UnstableTools.MODID)
public class UnstableTools {

  public static UnstableTools instance;
  public UnstableTools() {
    instance = this;
    EVENT_BUS.addListener(this::onDrops);
    EVENT_BUS.addListener(this::reload);
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    bus.addListener(Datagen::gather);
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
  }

  public static final String MODID = "unstabletools";

  public static class UnstableTier implements Tier {

    @Override
    public int getUses() {
      return 0;
    }

    @Override
    public float getSpeed() {
      return 8;
    }

    @Override
    public float getAttackDamageBonus() {
      return 8;
    }

    @Override
    public int getLevel() {
      return 4;
    }

    @Override
    public int getEnchantmentValue() {
      return 25;
    }

    @Nonnull
    @Override
    public Ingredient getRepairIngredient() {
      return Ingredient.of(unstable_ingot);
    }
  }

  public static final Tier UNSTABLE = new UnstableTier();

  public static class UnstableArmorMaterial implements ArmorMaterial {

    private static int[] array = new int[]{4, 7, 9, 4};

    @Override
    public int getDurabilityForSlot(@Nonnull EquipmentSlot slotIn) {
      return 0;
    }

    @Override
    public int getDefenseForSlot(@Nonnull EquipmentSlot slot) {
      return array[slot.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
      return 25;
    }

    @Nonnull
    @Override
    public SoundEvent getEquipSound() {
      return SoundEvents.ARMOR_EQUIP_DIAMOND;
    }

    @Nonnull
    @Override
    public Ingredient getRepairIngredient() {
      return Ingredient.of(unstable_ingot);
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

    @Override
    public float getKnockbackResistance() {
      return 1;
    }
  }

  public static final ArmorMaterial UNSTABLE_ARMOR = new UnstableArmorMaterial();
  public static final Set<Item> MOD_ITEMS = new HashSet<>();

  public static CreativeModeTab creativeTab = new CreativeModeTab(MODID) {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(unstable_pickaxe);
    }
  };

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<Block> event) {
      IForgeRegistry<Block> registry = event.getRegistry();
      register(new Block(Block.Properties.of(Material.METAL).strength(5, 6000).requiresCorrectToolForDrops()) {


                 @Override
                 @OnlyIn(Dist.CLIENT)
                 public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
                   return adjacentBlockState.getBlock() == this;
                 }

               }
              , "unstable_block", registry);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
      IForgeRegistry<Item> registry = event.getRegistry();

      Item.Properties properties = new Item.Properties().tab(creativeTab);

      register(new ItemUnstableIngot(properties), "unstable_ingot", registry);
      register(new ItemUnstableShears(properties), "unstable_shears", registry);

      register(new BlockItem(unstable_block, properties), unstable_block.getRegistryName().getPath(), registry);

      register(new ItemUnstableAxe(UNSTABLE, 9, -3, properties), "unstable_axe", registry);
      register(new ShovelItem(UNSTABLE, 3, -1.5f, properties), "unstable_spade", registry);
      register(new PickaxeItem(UNSTABLE, 1, -2.8f, properties), "unstable_pickaxe", registry);
      register(new SwordItem(UNSTABLE, 3, -2.4f, properties), "unstable_sword", registry);
      register(new ItemUnstableHoe(UNSTABLE, -4,0, properties), "unstable_hoe", registry);

      register(new FishingRodItem(properties), "unstable_fishing_rod", registry);
      //register(new UnstablePaxelItem(1, -1, UNSTABLE, AxeItem.EFFECTIVE_ON, properties), "unstable_paxel", registry);
      register(new UnstableBowItem(properties), "unstable_bow", registry);

      register(new ArmorItem(UNSTABLE_ARMOR, EquipmentSlot.HEAD, properties), "unstable_helmet", registry);
      register(new ArmorItem(UNSTABLE_ARMOR, EquipmentSlot.CHEST, properties), "unstable_chestplate", registry);
      register(new ArmorItem(UNSTABLE_ARMOR, EquipmentSlot.LEGS, properties), "unstable_leggings", registry);
      register(new ArmorItem(UNSTABLE_ARMOR, EquipmentSlot.FEET, properties), "unstable_boots", registry);
      register(new DivisionSignItem(properties,false), "inactive_division_sign", registry);
      register(new DivisionSignItem(properties,false), "division_sign", registry);
      register(new DivisionSignItem(properties,true), "stable_division_sign", registry);
    }

    @SubscribeEvent
    public static void registerSerials(RegistryEvent.Register<RecipeSerializer<?>> event) {
      register(new SimpleRecipeSerializer<>(RecipeDivision::new), "division", event.getRegistry());
    }

    private static <T extends IForgeRegistryEntry<T>> void register(T obj, String name, IForgeRegistry<T> registry) {
      registry.register(obj.setRegistryName(new ResourceLocation(MODID, name)));
      if (obj instanceof Item) MOD_ITEMS.add((Item) obj);
    }
  }

  public static void onBlockDrops(Level worldIn, BlockPos pos, ItemStack stackToSpawn, Entity entity, ItemStack stack) {
    if (entity instanceof Player) {
      Player player = (Player) entity;
      if (stack.getItem() instanceof ItemUnstableShears) {
        player.addItem(stackToSpawn);
      }
    }
  }

  public final ConversionManager manager = new ConversionManager();

  private void reload(AddReloadListenerEvent event) {
    event.addListener(manager);
  }

  private void onDrops(LivingDropsEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity instanceof WitherBoss && event.getSource().getEntity() instanceof Player) {

      ItemStack itemStackToDrop = new ItemStack(inactive_division_sign);
      event.getDrops().add(new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), itemStackToDrop));
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
    public static final Item unstable_hoe = null;
    public static final RecipeSerializer<?> division = null;
  }
}
