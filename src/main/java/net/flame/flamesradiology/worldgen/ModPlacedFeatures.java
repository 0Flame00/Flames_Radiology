package net.flame.flamesradiology.worldgen;

import net.flame.flamesradiology.FlamesRadiology;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;

import java.util.List;

public class ModPlacedFeatures {
    
    // Ore Placed Features
    public static final ResourceKey<PlacedFeature> LEAD_ORE_PLACED = registerKey("lead_ore_placed");
    public static final ResourceKey<PlacedFeature> BORON_ORE_PLACED = registerKey("boron_ore_placed");
    public static final ResourceKey<PlacedFeature> POLY_HALITE_ORE_PLACED = registerKey("poly_halite_ore_placed");
    
    // Raw Block Placed Features (rarer)
    public static final ResourceKey<PlacedFeature> RAW_LEAD_BLOCK_PLACED = registerKey("raw_lead_block_placed");
    public static final ResourceKey<PlacedFeature> RAW_BORON_BLOCK_PLACED = registerKey("raw_boron_block_placed");
    public static final ResourceKey<PlacedFeature> RAW_POLY_HALITE_BLOCK_PLACED = registerKey("raw_poly_halite_block_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // Ore Placements (more common)
        register(context, LEAD_ORE_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_LEAD_ORE),
                ModOrePlacement.commonOrePlacement(20, // veins per chunk (increased for visibility)
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32)))); // Gold ore range

        register(context, BORON_ORE_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_BORON_ORE),
                ModOrePlacement.commonOrePlacement(25, // veins per chunk (increased for visibility)
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(256)))); // Iron ore range

        register(context, POLY_HALITE_ORE_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_POLY_HALITE_ORE),
                ModOrePlacement.commonOrePlacement(15, // veins per chunk (increased for visibility)
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));

        // Raw Block Placements (rarer than ores)
        register(context, RAW_LEAD_BLOCK_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_RAW_LEAD_BLOCK),
                ModOrePlacement.rareOrePlacement(4, // veins per chunk (less than ore)
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32)))); // Same as lead ore

        register(context, RAW_BORON_BLOCK_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_RAW_BORON_BLOCK),
                ModOrePlacement.rareOrePlacement(3, // veins per chunk (less than ore)
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(256)))); // Same as boron ore

        register(context, RAW_POLY_HALITE_BLOCK_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_RAW_POLY_HALITE_BLOCK),
                ModOrePlacement.rareOrePlacement(2, // veins per chunk (less than ore)
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64)))); // Same as poly halite ore
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(FlamesRadiology.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
