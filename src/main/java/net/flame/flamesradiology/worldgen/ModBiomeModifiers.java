package net.flame.flamesradiology.worldgen;

import net.flame.flamesradiology.FlamesRadiology;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModBiomeModifiers {
    
    // Ore Biome Modifiers
    public static final ResourceKey<BiomeModifier> ADD_LEAD_ORE = registerKey("add_lead_ore");
    public static final ResourceKey<BiomeModifier> ADD_BORON_ORE = registerKey("add_boron_ore");
    public static final ResourceKey<BiomeModifier> ADD_POLY_HALITE_ORE = registerKey("add_poly_halite_ore");
    
    // Raw Block Biome Modifiers
    public static final ResourceKey<BiomeModifier> ADD_RAW_LEAD_BLOCK = registerKey("add_raw_lead_block");
    public static final ResourceKey<BiomeModifier> ADD_RAW_BORON_BLOCK = registerKey("add_raw_boron_block");
    public static final ResourceKey<BiomeModifier> ADD_RAW_POLY_HALITE_BLOCK = registerKey("add_raw_poly_halite_block");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        // Add ores to overworld biomes
        context.register(ADD_LEAD_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.LEAD_ORE_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_BORON_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.BORON_ORE_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_POLY_HALITE_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.POLY_HALITE_ORE_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        // Add raw blocks to overworld biomes (rarer)
        context.register(ADD_RAW_LEAD_BLOCK, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.RAW_LEAD_BLOCK_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_RAW_BORON_BLOCK, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.RAW_BORON_BLOCK_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_RAW_POLY_HALITE_BLOCK, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.RAW_POLY_HALITE_BLOCK_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(FlamesRadiology.MOD_ID, name));
    }
}
