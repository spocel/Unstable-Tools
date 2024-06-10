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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import com.google.common.collect.Multimap;
import java.util.UUID;

public class ItemSoul extends Item {

    public ItemSoul(){
        /*setUnlocalizedName("unstabletools:soul_fragment");*/
        /*setTextureName("extrautils:mini-soul");*/
        /*setCreativeTab(ExtraUtils2.creativeTabExtraUtils);*/
        setMaxStackSize(1);
        setHasSubtypes(true);
    }
    private static final UUID MAX_HEALTH_MODIFIER_ID = UUID.fromString("8f3c82ec-5b32-4b1b-8957-4f6fba3f89c9");
    private static final UUID MAX_HEALTH_MODIFIER_ID1 = UUID.fromString("8f3c82ec-5b32-4b1b-8957-4f6fba3f89c8");
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        double l=0.0D;
        if(itemstack.getItemDamage()==1) return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        if (!worldIn.isRemote) {
            IAttributeInstance maxHealthAttribute = playerIn.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
            if (maxHealthAttribute.getModifier(MAX_HEALTH_MODIFIER_ID) == null) {
                AttributeModifier maxHealthModifier = new AttributeModifier(MAX_HEALTH_MODIFIER_ID, "Health boost", 0.1, 2);
                maxHealthAttribute.applyModifier(maxHealthModifier);
            } else {
                AttributeModifier maxHealthModifier = maxHealthAttribute.getModifier(MAX_HEALTH_MODIFIER_ID);
                l=maxHealthModifier.getAmount();
                l+=0.1D;
                maxHealthAttribute.removeModifier(maxHealthModifier);/*
                double increaseAmount = maxHealthAttribute.getAttributeValue()*0.1;
                maxHealthModifier = new AttributeModifier(MAX_HEALTH_MODIFIER_ID, "Health boost",increaseAmount, 1);*/
                maxHealthAttribute.applyModifier(new AttributeModifier(MAX_HEALTH_MODIFIER_ID, "Health boost",l, 2));
                /*player.addChatComponentMessage()*/
            }
            itemstack.shrink(1);
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void onCreated(ItemStack stack,World worldIn, EntityPlayer playerIn){

        stack.setItemDamage(1);
        if (!worldIn.isRemote) {
            double l=0.0D;
            IAttributeInstance maxHealthAttribute = playerIn.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
            /*if(maxHealthModifier!=null)
                l=maxHealthModifier.getAmount();
            l-=0.1D;
            double c=Math.min(Math.min(maxHealthAttribute.getBaseValue()*(1.0D+l),maxHealthAttribute.getAttributeValue()),20.0D*(1.0D+l));
            if( c >=6.0D){
                stack.setItemDamage(0);
                maxHealthAttribute.removeModifier(maxHealthModifier);
                maxHealthAttribute.applyModifier(new AttributeModifier(MAX_HEALTH_MODIFIER_ID, "Health boost",l, 2));
            }*/
            if (maxHealthAttribute.getModifier(MAX_HEALTH_MODIFIER_ID) == null) {
                AttributeModifier maxHealthModifier = new AttributeModifier(MAX_HEALTH_MODIFIER_ID, "Health boost", -0.1, 2);
                maxHealthAttribute.applyModifier(maxHealthModifier);
            } else {
                AttributeModifier maxHealthModifier = maxHealthAttribute.getModifier(MAX_HEALTH_MODIFIER_ID);
                l = maxHealthModifier.getAmount();
                l -= 0.1D;
                double c=Math.min(Math.min(maxHealthAttribute.getBaseValue()*(1.0D+l),maxHealthAttribute.getAttributeValue()),20.0D*(1.0D+l));
                if( c >=6.0D) {
                    stack.setItemDamage(0);
                    maxHealthAttribute.removeModifier(maxHealthModifier);
                    maxHealthAttribute.applyModifier(new AttributeModifier(MAX_HEALTH_MODIFIER_ID, "Health boost", l, 2));
                    /*player.addChatComponentMessage()*/
                }
            }
        }
    }
}

