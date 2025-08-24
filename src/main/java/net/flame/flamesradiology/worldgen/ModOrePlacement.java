package net.flame.flamesradiology.worldgen;

import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;

import java.util.List;

public class ModOrePlacement {
    
    public static List<PlacementModifier> orePlacement(PlacementModifier countPlacement, PlacementModifier heightRangePlacement) {
        return List.of(countPlacement, InSquarePlacement.spread(), heightRangePlacement, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRangePlacement) {
        return orePlacement(CountPlacement.of(count), heightRangePlacement);
    }

    public static List<PlacementModifier> rareOrePlacement(int count, PlacementModifier heightRangePlacement) {
        return orePlacement(CountPlacement.of(count), heightRangePlacement);
    }
}
