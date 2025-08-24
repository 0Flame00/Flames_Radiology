package net.flame.flamesradiology.common.init;

import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties POTASSIUM_IODIDE = new FoodProperties.Builder()
            .nutrition(0)
            .saturationModifier(0.0f)
            .alwaysEdible()
            .build();
}
