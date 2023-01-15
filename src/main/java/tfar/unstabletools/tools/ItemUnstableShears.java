package tfar.unstabletools.tools;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

//@Mod.EventBusSubscriber(modid = UnstableTools.MODID)
import net.minecraft.item.Item.Properties;

public class ItemUnstableShears extends ShearsItem {

  public ItemUnstableShears(Properties builder) {
    super(builder);
  }

  @Override
  public int getHarvestLevel(ItemStack stack,@Nonnull ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
    return 3;
  }

  private static final Set<ToolType> pickaxe = Sets.newHashSet(ToolType.PICKAXE);


  @Nonnull
  @Override
  public Set<ToolType> getToolTypes(ItemStack stack) {
    return pickaxe;
  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    return 20;
  }

  @Override
  public boolean isCorrectToolForDrops(BlockState block) {
    return true;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
    return super.canApplyAtEnchantingTable(stack,enchantment) || enchantment == Enchantments.SILK_TOUCH;
  }

  //doesn't work
  /*@SubscribeEvent
  @SuppressWarnings("unused")
  public static void itemDrops(BlockEvent.HarvestDropsEvent e) {
    PlayerEntity player = e.getHarvester();
    if (player != null && player.getHeldItemMainhand().getItem() instanceof ItemUnstableShears)
      e.getDrops().removeIf(player::addItemStackToInventory);
  }*/
}