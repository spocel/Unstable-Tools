package tfar.unstabletools.tools;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.Set;

import net.minecraft.item.Item.Properties;

public class UnstablePaxelItem extends ToolItem {
  public UnstablePaxelItem(float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Properties builder) {
    super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builder);
  }

  protected static final Set<ToolType> tooltypes = Sets.newHashSet(ToolType.PICKAXE,ToolType.AXE,ToolType.SHOVEL);

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (!isSelected || !(entityIn instanceof PlayerEntity) || worldIn.isClientSide) return;
    ((PlayerEntity) entityIn).getFoodData().eat(1, 0.2F);  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    Material material = state.getMaterial();
    return material != Material.WOOD && material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.BAMBOO ? super.getDestroySpeed(stack, state) : this.speed;
  }

  @Override
  public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
    if (entity instanceof LivingEntity) {
      if (((LivingEntity) entity).getMobType() == CreatureAttribute.UNDEAD)
        entity.hurt(DamageSource.playerAttack(player), 8);
      else ((LivingEntity) entity).heal(8);
      player.addEffect(new EffectInstance(Effects.HUNGER,20,4));
    }
    return true;
  }

  @Nonnull
  @Override
  public Set<ToolType> getToolTypes(ItemStack stack) {
    return tooltypes;
  }
}
