package tfar.unstabletools;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tfar.unstabletools.init.ModBlocks;
import tfar.unstabletools.init.ModItems;
import tfar.unstabletools.item.IItemColored;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT,modid = UnstableTools.MODID)
public class Client {
  @SubscribeEvent
  public static void colors(RegisterColorHandlersEvent.Item e) {
    ModItems.getItems().stream().filter(IItemColored.class::isInstance).forEach(item -> e.register(((IItemColored) item)::getColor, item));
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.unstable_block, RenderType.cutout());
    for (Enchantment enchantment : Registry.ENCHANTMENT) {
      if (enchantment.getMaxLevel() > 1)  {
        System.out.println(Registry.ENCHANTMENT.getKey(enchantment));
      }
    }
  }
}
