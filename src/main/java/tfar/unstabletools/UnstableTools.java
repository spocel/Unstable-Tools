package tfar.unstabletools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import tfar.unstabletools.crafting.Config;
import tfar.unstabletools.crafting.ConversionManager;
import tfar.unstabletools.datagen.Datagen;
import tfar.unstabletools.init.ModBlocks;
import tfar.unstabletools.init.ModItems;
import tfar.unstabletools.init.ModRecipeSerializer;
import tfar.unstabletools.item.tools.ItemUnstableShears;

import javax.annotation.Nonnull;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

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
      return Ingredient.of(ModItems.unstable_ingot);
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
      return Ingredient.of(ModItems.unstable_ingot);
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

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    @SubscribeEvent
    public static void registerBlock(RegisterEvent event) {
      event.register(Registries.BLOCK, new ResourceLocation(MODID, "unstable_block"), () -> ModBlocks.unstable_block);
      event.register(Registries.BLOCK, new ResourceLocation(MODID, "ethereal_glass"), () -> ModBlocks.ethereal_glass);

      event.register(Registries.ITEM, new ResourceLocation(MODID, "unstable_ingot"), () -> ModItems.unstable_ingot);
      event.register(Registries.ITEM, new ResourceLocation(MODID,"unstable_shears"), () -> ModItems.unstable_shears);

      event.register(Registries.ITEM,new ResourceLocation(MODID,"unstable_block"), () -> ModItems.unstable_block);
      event.register(Registries.ITEM,new ResourceLocation(MODID,"ethereal_glass"), () -> ModItems.ethereal_glass);

      event.register(Registries.ITEM, new ResourceLocation(MODID, "unstable_axe"), () -> ModItems.unstable_axe);
      event.register(Registries.ITEM, new ResourceLocation(MODID,"unstable_shovel"), () -> ModItems.unstable_shovel);
      event.register(Registries.ITEM, new ResourceLocation(MODID,"unstable_pickaxe"), () -> ModItems.unstable_pickaxe);
      event.register(Registries.ITEM,new ResourceLocation(MODID,"unstable_sword"),() -> ModItems.unstable_sword);
      event.register(Registries.ITEM, new ResourceLocation(MODID,"unstable_hoe"), () -> ModItems.unstable_hoe);

      event.register(Registries.ITEM,new ResourceLocation(MODID, "unstable_fishing_rod"), () -> ModItems.unstable_fishing_rod);
      event.register(Registries.ITEM,new ResourceLocation(MODID,"unstable_bow"), () -> ModItems.unstable_bow);

      event.register(Registries.ITEM,new ResourceLocation(MODID, "unstable_helmet"), () -> ModItems.unstable_helmet);
      event.register(Registries.ITEM,new ResourceLocation(MODID, "unstable_chestplate"), () -> ModItems.unstable_chestplate);
      event.register(Registries.ITEM,new ResourceLocation(MODID,"unstable_leggings"),() -> ModItems.unstable_leggings);
      event.register(Registries.ITEM,new ResourceLocation(MODID,"unstable_boots"), () -> ModItems.unstable_boots);
      event.register(Registries.ITEM,new ResourceLocation(MODID,"inactive_division_sign"), () -> ModItems.inactive_division_sign);
      event.register(Registries.ITEM,new ResourceLocation(MODID,"division_sign"), () -> ModItems.division_sign);
      event.register(Registries.ITEM,new ResourceLocation(MODID,"stable_division_sign"), () -> ModItems.stable_division_sign);

      event.register(Registries.RECIPE_SERIALIZER, new ResourceLocation(MODID,"division"), () -> ModRecipeSerializer.division);

    }

    @SubscribeEvent
    public static void registerTab(CreativeModeTabEvent.Register event) {
      event.registerCreativeModeTab(new ResourceLocation(MODID,MODID),
              builder -> builder.title(Component.translatable("itemGroup.unstabletools"))
                      .icon(ModItems.unstable_pickaxe::getDefaultInstance)
                      .displayItems((pEnabledFeatures, pOutput, pDisplayOperatorCreativeTab) -> {
                        for (Item item : ModItems.getItems()) {
                          pOutput.accept(item);
                        }
                      }).build()
      );
    }
  }

  public static void onBlockDrops(Level worldIn, BlockPos pos, ItemStack stackToSpawn, Entity entity, ItemStack stack) {
    if (entity instanceof Player player) {
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
    LivingEntity entity = event.getEntity();
    if (entity instanceof WitherBoss && event.getSource().getEntity() instanceof Player) {

      ItemStack itemStackToDrop = new ItemStack(ModItems.inactive_division_sign);
      event.getDrops().add(new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), itemStackToDrop));
    }
  }

}
