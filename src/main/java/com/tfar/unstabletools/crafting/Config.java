package com.tfar.unstabletools.crafting;

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

    ServerConfig(ForgeConfigSpec.Builder builder) {
      List<String> strings = new ArrayList<>();
      strings.add(ContainerType.CRAFTING.getRegistryName().toString());
      strings.add("fastbench:fast_crafting");
      builder.push("general");
      allowed_containers = builder
              .comment("Allowed Container Types")
              .translation("text.unstabletools.config.allowed_containers")
              .define("containers", strings, o -> o instanceof String);
      builder.pop();
    }
  }
}
