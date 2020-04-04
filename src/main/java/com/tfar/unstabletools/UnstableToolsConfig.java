package com.tfar.unstabletools;

import net.minecraft.inventory.ContainerWorkbench;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Config(modid = UnstableTools.MODID)
@Mod.EventBusSubscriber()
public class UnstableToolsConfig {

	@Config.Name("allowed container classes")
	public static String[] allowed_containers = new String[]{ContainerWorkbench.class.getName(),
					"shadows.fastbench.gui.ContainerFastBench",
					"shadows.fastbench.gui.ClientContainerFastBench"
	};

	@Config.Ignore
	public static Set<Class<?>> allowed_container_classes = new HashSet<>();

	@SubscribeEvent
	public static void config(ConfigChangedEvent.OnConfigChangedEvent e) {
		if (e.getModID().equals(UnstableTools.MODID)) {
			allowed_container_classes.clear();
			Arrays.stream(allowed_containers).map(UnstableToolsConfig::clazz)
							.forEach(allowed_container_classes::add);
		}
	}

	public static void sync(){
		allowed_container_classes.clear();
		Arrays.stream(allowed_containers).map(UnstableToolsConfig::clazz)
						.forEach(allowed_container_classes::add);
	}

	public static Class<?> clazz(String s) {
		try {
			return Class.forName(s);
		} catch (ClassNotFoundException e) {
			UnstableTools.logger.warn(s +" not found");
		}
		return null;
	}
}
