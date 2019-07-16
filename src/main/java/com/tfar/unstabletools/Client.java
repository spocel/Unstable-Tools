package com.tfar.unstabletools;

import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Client {
  @SubscribeEvent
  public static void colors(ColorHandlerEvent.Item e){
    ItemColors itemColors = e.getItemColors();
    itemColors.registerItemColorHandler(ObjectHolders.unstableIngot::getColor,(Item)ObjectHolders.unstableIngot);
    itemColors.registerItemColorHandler(ObjectHolders.divisionSign::getColor,(Item)ObjectHolders.divisionSign);
  }
}
