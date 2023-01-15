package tfar.unstabletools.crafting;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Config {

  public static final ServerConfig SERVER;
  public static final ForgeConfigSpec COMMON_SPEC;

  static {
    final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
    COMMON_SPEC = specPair.getRight();
    SERVER = specPair.getLeft();
  }

  public static class ServerConfig {

    public static ForgeConfigSpec.ConfigValue<List<String>> allowed_containers;
    public static ForgeConfigSpec.BooleanValue cursed_earth_integration;
    public static ForgeConfigSpec.IntValue timer;
    public static ForgeConfigSpec.IntValue uses;

    ServerConfig(ForgeConfigSpec.Builder builder) {
      List<String> strings = new ArrayList<>();
      strings.add(ContainerType.CRAFTING.getRegistryName().toString());
      strings.add("fastbench:fastbench");
      builder.push("general");
      allowed_containers = builder
              .comment("Allowed Container Types")
              .translation("text.unstabletools.config.allowed_containers")
              .define("containers", strings, List.class::isInstance);
      cursed_earth_integration = builder
              .comment("Enable integration with Cursed Earth")
              .translation("text.unstabletools.config.cursed_earth_integration")
              .define("cursed earth integration",true);
      timer = builder
              .comment("Time before explosion in ticks")
              .translation("text.unstabletools.config.timer")
              .defineInRange("timer",200, 1,Integer.MAX_VALUE);

      uses = builder
              .comment("Number of uses when charged")
              .translation("text.unstabletools.config.uses")
              .defineInRange("uses",256, 1,Integer.MAX_VALUE);
      builder.pop();
    }
  }
}
