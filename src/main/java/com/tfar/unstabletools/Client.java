package com.tfar.unstabletools;

import com.tfar.unstabletools.item.IItemColored;
import com.tfar.unstabletools.item.ItemDivisionSign;
import com.tfar.unstabletools.item.ItemUnstableIngot;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.tfar.unstabletools.UnstableTools.ObjectHolders.division_sign;
import static com.tfar.unstabletools.UnstableTools.ObjectHolders.unstable_ingot;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT,modid = UnstableTools.MODID)
public class Client {
  @SubscribeEvent
  public static void colors(ColorHandlerEvent.Item e) {
    UnstableTools.MOD_ITEMS.stream().filter(item -> item instanceof IItemColored).forEach(item -> e.getItemColors().register(((IItemColored) item)::getColor, item));
  }
}
