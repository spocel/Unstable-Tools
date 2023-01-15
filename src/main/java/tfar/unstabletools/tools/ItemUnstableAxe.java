package tfar.unstabletools.tools;

import com.google.common.collect.Sets;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.Set;

public class ItemUnstableAxe extends AxeItem {

  public ItemUnstableAxe(IItemTier materialIn, float damage, float attackSpeed, Properties properties) {
    super(materialIn, damage, attackSpeed, properties);
  }

  private static final Set<ToolType> axe = Sets.newHashSet(ToolType.AXE);

  @Nonnull
  @Override
  public Set<ToolType> getToolTypes(ItemStack stack) {
    return axe;
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entity, int itemSlot, boolean isSelected) {
    if (!isSelected || !(entity instanceof PlayerEntity) || worldIn.isRemote) return;
    ((PlayerEntity) entity).getFoodStats().addStats(1, 0.2F);
  }

  @Override
  public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
    if (entity instanceof LivingEntity) {
      LivingEntity livingEntity = (LivingEntity) entity;
      if (livingEntity.getCreatureAttribute() == CreatureAttribute.UNDEAD)
        entity.attackEntityFrom(DamageSource.causePlayerDamage(player), 8);
      else livingEntity.heal(8);
      player.addPotionEffect(new EffectInstance(Effects.HUNGER,20,4));
    }
    return true;
  }
}


