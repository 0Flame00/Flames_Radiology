package net.flame.flamesradiology.datagen;

import net.flame.flamesradiology.FlamesRadiology;
import net.flame.flamesradiology.common.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        // Basic crafting components
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FABRIC.get())
            .pattern("SSS")
            .pattern("SSS")
            .define('S', Items.STRING)
            .unlockedBy("has_string", has(Items.STRING))
            .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RUBBER.get())
            .pattern("CCC")
            .pattern("SSS")
            .define('C', Items.COAL)
            .define('S', Items.SLIME_BALL)
            .unlockedBy("has_coal", has(Items.COAL))
            .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GLASS_TUBE.get())
            .pattern("GGG")
            .define('G', Items.GLASS)
            .unlockedBy("has_glass", has(Items.GLASS))
            .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ACTIVATED_CARBON.get())
            .pattern("CR")
            .pattern("RC")
            .define('C', Ingredient.of(Items.COAL, Items.CHARCOAL))
            .define('R', Items.REDSTONE)
            .unlockedBy("has_redstone", has(Items.REDSTONE))
            .save(recipeOutput);

        // Advanced items
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GEIGER_COUNTER.get())
            .pattern("IGI")
            .pattern("RBR")
            .pattern("III")
            .define('I', Items.IRON_INGOT)
            .define('R', Items.REDSTONE)
            .define('B', ModItems.BORON_INGOT.get())
            .define('G', ModItems.GLASS_TUBE.get())
            .unlockedBy("has_boron_ingot", has(ModItems.BORON_INGOT.get()))
            .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GAS_FILTER.get())
            .pattern("FIF")
            .pattern("IAI")
            .pattern("FIF")
            .define('F', ModItems.FABRIC.get())
            .define('I', Items.IRON_INGOT)
            .define('A', ModItems.ACTIVATED_CARBON.get())
            .unlockedBy("has_fabric", has(ModItems.FABRIC.get()))
            .save(recipeOutput);

        // Hazmat armor
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HAZMAT_HELMET.get())
            .pattern("FFF")
            .pattern("FHF")
            .pattern("GLG")
            .define('F', ModItems.FABRIC.get())
            .define('L', ModItems.LEAD_INGOT.get())
            .define('G', ModItems.GAS_FILTER.get())
            .define('H', Items.LEATHER_HELMET)
            .unlockedBy("has_fabric", has(ModItems.FABRIC.get()))
            .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HAZMAT_CHESTPLATE.get())
            .pattern("LRL")
            .pattern("FHF")
            .pattern("FFF")
            .define('L', ModItems.LEAD_INGOT.get())
            .define('R', ModItems.RUBBER.get())
            .define('F', ModItems.FABRIC.get())
            .define('H', Items.LEATHER_CHESTPLATE)
            .unlockedBy("has_fabric", has(ModItems.FABRIC.get()))
            .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HAZMAT_LEGGINGS.get())
            .pattern("FLF")
            .pattern("FHF")
            .pattern("R R")
            .define('R', ModItems.RUBBER.get())
            .define('F', ModItems.FABRIC.get())
            .define('H', Items.LEATHER_LEGGINGS)
            .define('L', ModItems.LEAD_INGOT.get())
            .unlockedBy("has_fabric", has(ModItems.FABRIC.get()))
            .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HAZMAT_BOOTS.get())
            .pattern("RLR")
            .pattern("RHR")
            .pattern("RLR")
            .define('R', ModItems.RUBBER.get())
            .define('H', Items.LEATHER_BOOTS)
            .define('L', ModItems.LEAD_INGOT.get())
            .unlockedBy("has_rubber", has(ModItems.RUBBER.get()))
            .save(recipeOutput);

        // Smelting recipes
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.RAW_BORON.get()), 
                RecipeCategory.MISC, ModItems.BORON_INGOT.get(), 0.7f, 200)
            .unlockedBy("has_raw_boron", has(ModItems.RAW_BORON.get()))
            .save(recipeOutput);

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ModItems.RAW_BORON.get()), 
                RecipeCategory.MISC, ModItems.BORON_INGOT.get(), 0.7f, 100)
            .unlockedBy("has_raw_boron", has(ModItems.RAW_BORON.get()))
            .save(recipeOutput, FlamesRadiology.MOD_ID + ":boron_ingot_from_raw_boron_blasting");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.RAW_LEAD.get()), 
                RecipeCategory.MISC, ModItems.LEAD_INGOT.get(), 1.0f, 200)
            .unlockedBy("has_raw_lead", has(ModItems.RAW_LEAD.get()))
            .save(recipeOutput);

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ModItems.RAW_LEAD.get()), 
                RecipeCategory.MISC, ModItems.LEAD_INGOT.get(), 1.0f, 100)
            .unlockedBy("has_raw_lead", has(ModItems.RAW_LEAD.get()))
            .save(recipeOutput, FlamesRadiology.MOD_ID + ":lead_ingot_from_raw_lead_blasting");

        // Iodine from 15 kelp in blast furnace
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.IODINE.get())
            .requires(Items.KELP, 15)
            .unlockedBy("has_kelp", has(Items.KELP))
            .save(recipeOutput, FlamesRadiology.MOD_ID + ":iodine_from_kelp_shapeless");

        // Potassium Iodide with cross pattern
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.POTASSIUM_IODIDE.get())
            .pattern(" W ")
            .pattern("WIW")
            .pattern(" W ")
            .define('W', Items.POTION)
            .define('I', ModItems.IODINE.get())
            .unlockedBy("has_iodine", has(ModItems.IODINE.get()))
            .save(recipeOutput);

        // Netherite upgrades
        netheriteSmithing(recipeOutput, ModItems.HAZMAT_HELMET.get(), RecipeCategory.COMBAT, ModItems.NETHERITE_HAZMAT_HELMET.get());
        netheriteSmithing(recipeOutput, ModItems.HAZMAT_CHESTPLATE.get(), RecipeCategory.COMBAT, ModItems.NETHERITE_HAZMAT_CHESTPLATE.get());
        netheriteSmithing(recipeOutput, ModItems.HAZMAT_LEGGINGS.get(), RecipeCategory.COMBAT, ModItems.NETHERITE_HAZMAT_LEGGINGS.get());
        netheriteSmithing(recipeOutput, ModItems.HAZMAT_BOOTS.get(), RecipeCategory.COMBAT, ModItems.NETHERITE_HAZMAT_BOOTS.get());
    }
}
