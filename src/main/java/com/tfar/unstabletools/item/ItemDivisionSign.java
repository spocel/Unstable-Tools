package com.tfar.unstabletools.item;

import com.tfar.unstabletools.crafting.IDivisionItem;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
@Mod.EventBusSubscriber
public class ItemDivisionSign extends Item implements IDivisionItem,IItemColored {

  @Override
  public boolean hasContainerItem(ItemStack stack) {
    return true;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return stack.hasTagCompound() && (stack.getTagCompound().getBoolean("activated") || stack.getTagCompound().getBoolean("activated"));
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (!stack.hasTagCompound()) {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setBoolean("activated", false);
      stack.setTagCompound(nbt);
    }
  }

  @Override
  @Nonnull
  public ItemStack getContainerItem(ItemStack itemStack) {
    return damage(itemStack.copy());
  }

  public static ItemStack damage(ItemStack stack) {
    NBTTagCompound nbt = stack.getTagCompound();
    boolean stable = stack.getTagCompound().getBoolean("stable");
    if (stable)return stack;
    int d = nbt.getInteger("d");
    d--;
    nbt.setInteger("d", d);
    stack.setTagCompound(nbt);
    return stack;
  }

  @Override
  @Nonnull
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (hand == EnumHand.OFF_HAND || world.isRemote)return EnumActionResult.FAIL;
    Block block = world.getBlockState(pos).getBlock();
    if (block != Blocks.ENCHANTING_TABLE) return EnumActionResult.FAIL;
    long time = world.getWorldInfo().getWorldTime() % 24000;

    boolean correctTime = false;
    if (time <= 17500) player.sendMessage(new TextComponentTranslation("unstabletools.early"));
    else if (time <= 18500) {player.sendMessage(new TextComponentTranslation("unstabletools.ontime"));correctTime=true;}
    else player.sendMessage(new TextComponentTranslation("unstabletools.late"));
    boolean circle = true;
    for (int i = -1; i < 2; i++) {
      for (int j = -1; j < 2; j++) {
        if (i == 0 && j == 0) continue;
        BlockPos pos1 = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ() + j);
        if(world.getBlockState(pos1).getBlock() != Blocks.REDSTONE_WIRE)circle = false;
      }
    }

    if (!circle)player.sendMessage(new TextComponentTranslation("unstabletools.incomplete"));
    boolean skyVisible = world.canSeeSky(pos.up());
    if (!skyVisible)player.sendMessage(new TextComponentTranslation("unstabletools.nosky"));

    if(correctTime && circle && skyVisible)player.sendMessage(new TextComponentTranslation("unstabletools.ready"));

    return EnumActionResult.PASS;
  }



  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    if (worldIn == null) return;
    if (!stack.hasTagCompound()) return;
    boolean stable = stack.getTagCompound().getBoolean("stable");
    if (stable){tooltip.add("Stable");return;}
    boolean activated = stack.getTagCompound().getBoolean("activated");
    tooltip.add("Activated: " + activated);
    if (!activated) return;
    tooltip.add("Uses Left: " + stack.getTagCompound().getInteger("d"));
  }

  @SubscribeEvent
  public static void onSacrifice(LivingDeathEvent e){
    if (!(e.getSource().getTrueSource() instanceof EntityPlayer))return;
    EntityPlayer player = (EntityPlayer)e.getSource().getTrueSource();
    EntityLivingBase sacrifice = e.getEntityLiving();
    World world = sacrifice.world;
    BlockPos pos = sacrifice.getPosition();
    if (!world.canSeeSky(pos))return;
    Block block = world.getBlockState(pos.down()).getBlock();
    if (block != Blocks.ENCHANTING_TABLE)return;

    for (int i = -1; i < 2; i++) {
      for (int j = -1; j < 2; j++) {
        if (i == 0 && j == 0) continue;
        BlockPos pos1 = new BlockPos(pos.down().getX() + i, pos.down().getY(), pos.down().getZ() + j);
        if(world.getBlockState(pos1).getBlock() != Blocks.REDSTONE_WIRE)return;
      }
    }

    long time = world.getWorldInfo().getWorldTime() % 24000;
    if (time <= 17500 || time > 18500)return;

    for (Slot slot : player.inventoryContainer.inventorySlots){
      ItemStack stack = slot.getStack();
      if (!(stack.getItem() instanceof IDivisionItem))continue;
      stack.getTagCompound().setBoolean("activated",true);
      stack.getTagCompound().setInteger("d",256);
    }
    sacrifice.world.addWeatherEffect(new EntityLightningBolt(sacrifice.world, sacrifice.posX, sacrifice.posY, sacrifice.posZ, false));
  }

  @Override
  public int getColor(ItemStack stack, int tintIndex) {
    return (!stack.hasTagCompound()) ? 0xee0000 : (stack.getTagCompound().getBoolean("stable")) ? 0x50dd00 : 0xee0000;
  }
}
