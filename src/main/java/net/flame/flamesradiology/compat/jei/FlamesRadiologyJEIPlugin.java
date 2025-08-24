package net.flame.flamesradiology.compat.jei;

import javax.annotation.Nonnull;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.flame.flamesradiology.FlamesRadiology;
import net.flame.flamesradiology.common.init.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class FlamesRadiologyJEIPlugin implements IModPlugin {
    
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(FlamesRadiology.MOD_ID, "jei_plugin");
    }
    
    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        // Add information tooltips for crafting materials
        addComponentInfo(registration, ModItems.LEAD_INGOT.get(), "Heavy metal used in radiation shielding and hazmat armor!");
        addComponentInfo(registration, ModItems.BORON_INGOT.get(), "Used in radiation detection equipment like Geiger counters!");
        addComponentInfo(registration, ModItems.RAW_BORON.get(), "Raw boron ore that needs smelting. Used in electronics!");
        addComponentInfo(registration, ModItems.RAW_LEAD.get(), "Raw lead ore that needs smelting. Heavy radiation shielding material!");
        
        // Add information for protective equipment
        addProtectionInfo(registration, ModItems.HAZMAT_HELMET.get(), "Provides radiation protection when worn as full set. Requires gas filter!");
        addProtectionInfo(registration, ModItems.HAZMAT_CHESTPLATE.get(), "Part of hazmat protection suit. Wear full set for immunity!");
        addProtectionInfo(registration, ModItems.HAZMAT_LEGGINGS.get(), "Part of hazmat protection suit. Wear full set for immunity!");
        addProtectionInfo(registration, ModItems.HAZMAT_BOOTS.get(), "Part of hazmat protection suit. Wear full set for immunity!");
        
        addProtectionInfo(registration, ModItems.NETHERITE_HAZMAT_HELMET.get(), "Enhanced radiation protection. Requires gas filter!");
        addProtectionInfo(registration, ModItems.NETHERITE_HAZMAT_CHESTPLATE.get(), "Enhanced hazmat protection. Wear full set for immunity!");
        addProtectionInfo(registration, ModItems.NETHERITE_HAZMAT_LEGGINGS.get(), "Enhanced hazmat protection. Wear full set for immunity!");
        addProtectionInfo(registration, ModItems.NETHERITE_HAZMAT_BOOTS.get(), "Enhanced hazmat protection. Wear full set for immunity!");
        
        // Add information for utility items
        addUtilityInfo(registration, ModItems.GEIGER_COUNTER.get(), "Right-click to measure radiation levels in the area!");
        addUtilityInfo(registration, ModItems.GAS_FILTER.get(), "Right-click on hazmat helmet to replenish gas filter durability!");
        addUtilityInfo(registration, ModItems.POTASSIUM_IODIDE.get(), "Consume to reduce radiation exposure effects!");
        
        // Add information for crafting components
        addComponentInfo(registration, ModItems.GLASS_TUBE.get(), "Used in crafting electronic devices like Geiger counters!");
        addComponentInfo(registration, ModItems.RUBBER.get(), "Essential component for hazmat suit construction!");
        addComponentInfo(registration, ModItems.FABRIC.get(), "Base material for protective equipment!");
        addComponentInfo(registration, ModItems.ACTIVATED_CARBON.get(), "Key component in gas filter manufacturing!");
        addComponentInfo(registration, ModItems.POTASSIUM.get(), "Chemical element used in potassium iodide synthesis!");
        addComponentInfo(registration, ModItems.IODINE.get(), "Chemical element extracted from kelp, used in medicine!");
    }
    
    private void addProtectionInfo(IRecipeRegistration registration, net.minecraft.world.item.Item item, String info) {
        registration.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM_STACK,
            Component.literal("§a⚡ RADIATION PROTECTION ⚡§r"),
            Component.literal("§7" + info + "§r"));
    }
    
    private void addUtilityInfo(IRecipeRegistration registration, net.minecraft.world.item.Item item, String info) {
        registration.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM_STACK,
            Component.literal("§b⚙ UTILITY ITEM ⚙§r"),
            Component.literal("§7" + info + "§r"));
    }
    
    private void addComponentInfo(IRecipeRegistration registration, net.minecraft.world.item.Item item, String info) {
        registration.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM_STACK,
            Component.literal("§e🔧 CRAFTING COMPONENT 🔧§r"),
            Component.literal("§7" + info + "§r"));
    }
}