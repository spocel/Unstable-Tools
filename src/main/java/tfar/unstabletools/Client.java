package tfar.unstabletools;

import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import tfar.unstabletools.init.ModBlocks;
import tfar.unstabletools.init.ModItems;
import tfar.unstabletools.item.IItemColored;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT,modid = UnstableTools.MODID)
public class Client {
  @SubscribeEvent
  public static void colors(RegisterColorHandlersEvent.Item e) {
    ModItems.getItems().stream().filter(IItemColored.class::isInstance).forEach(item -> e.register(((IItemColored) item)::getColor, item));
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.unstable_block, RenderType.cutout());
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.ethereal_glass, RenderType.cutout());
  }
}
