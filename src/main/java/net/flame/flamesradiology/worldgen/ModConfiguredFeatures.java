package net.flame.flamesradiology.worldgen;

import net.flame.flamesradiology.FlamesRadiology;
import net.flame.flamesradiology.common.init.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {
    
    // Ore Features
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_LEAD_ORE = registerKey("lead_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_BORON_ORE = registerKey("boron_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_POLY_HALITE_ORE = registerKey("poly_halite_ore");
    
    // Raw Block Features (rarer than ores)
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_RAW_LEAD_BLOCK = registerKey("raw_lead_block");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_RAW_BORON_BLOCK = registerKey("raw_boron_block");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_RAW_POLY_HALITE_BLOCK = registerKey("raw_poly_halite_block");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        // Lead Ore Configuration
        List<OreConfiguration.TargetBlockState> overworldLeadOres = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.LEAD_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState())
        );

        // Boron Ore Configuration
        List<OreConfiguration.TargetBlockState> overworldBoronOres = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.BORON_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_BORON_ORE.get().defaultBlockState())
        );

        // Poly Halite Ore Configuration
        List<OreConfiguration.TargetBlockState> overworldPolyHaliteOres = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.POLY_HALITE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_POLY_HALITE_ORE.get().defaultBlockState())
        );

        // Raw Block Configurations (smaller veins, rarer)
        List<OreConfiguration.TargetBlockState> overworldRawLeadBlocks = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.RAW_LEAD_BLOCK.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldRawBoronBlocks = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.RAW_BORON_BLOCK.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldRawPolyHaliteBlocks = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.RAW_POLY_HALITE_BLOCK.get().defaultBlockState())
        );

        // Register Ore Features (larger veins)
        register(context, OVERWORLD_LEAD_ORE, Feature.ORE, new OreConfiguration(overworldLeadOres, 9));
        register(context, OVERWORLD_BORON_ORE, Feature.ORE, new OreConfiguration(overworldBoronOres, 9));
        register(context, OVERWORLD_POLY_HALITE_ORE, Feature.ORE, new OreConfiguration(overworldPolyHaliteOres, 9));

        // Register Raw Block Features (smaller veins)
        register(context, OVERWORLD_RAW_LEAD_BLOCK, Feature.ORE, new OreConfiguration(overworldRawLeadBlocks, 4));
        register(context, OVERWORLD_RAW_BORON_BLOCK, Feature.ORE, new OreConfiguration(overworldRawBoronBlocks, 4));
        register(context, OVERWORLD_RAW_POLY_HALITE_BLOCK, Feature.ORE, new OreConfiguration(overworldRawPolyHaliteBlocks, 4));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(FlamesRadiology.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
