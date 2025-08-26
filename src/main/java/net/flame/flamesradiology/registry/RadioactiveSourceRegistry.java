package net.flame.flamesradiology.registry;

import net.flame.flamesradiology.FlamesRadiology;
import net.flame.flamesradiology.common.init.ModBlocks;
import net.flame.flamesradiology.common.init.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class RadioactiveSourceRegistry {
    private static final Map<ResourceLocation, Double> RADIOACTIVE_SOURCES = new HashMap<>();
    private static final Map<String, Double> MOD_RADIOACTIVE_SOURCES = new HashMap<>();
    private static final Map<Block, Double> RADIOACTIVE_BLOCKS = new HashMap<>();
    private static final Map<Item, Double> RADIOACTIVE_ITEMS = new HashMap<>();
    
    /**
     * Register a radioactive source by ResourceLocation
     * @param itemId The ResourceLocation of the item/block
     * @param intensity Radiation intensity (higher = more radioactive)
     */
    public static void registerRadioactiveSource(ResourceLocation itemId, double intensity) {
        RADIOACTIVE_SOURCES.put(itemId, intensity);
    }
    
    /**
     * Register a radioactive source by string name (for mod compatibility)
     * @param itemName The full item name (e.g., "mekanism:uranium_ore")
     * @param intensity Radiation intensity (higher = more radioactive)
     */
    public static void registerRadioactiveSourceByName(String itemName, double intensity) {
        MOD_RADIOACTIVE_SOURCES.put(itemName, intensity);
        FlamesRadiology.LOGGER.debug("Registered radioactive source: {} with intensity: {}", itemName, intensity);
    }
    
    /**
     * Register a radioactive block with its radiation intensity
     * @param block The block to register
     * @param intensity Radiation intensity (higher = more radioactive)
     */
    public static void registerRadioactiveBlock(Block block, double intensity) {
        RADIOACTIVE_BLOCKS.put(block, intensity);
    }
    
    /**
     * Register a radioactive item with its radiation intensity
     * @param item The item to register
     * @param intensity Radiation intensity (higher = more radioactive)
     */
    public static void registerRadioactiveItem(Item item, double intensity) {
        RADIOACTIVE_ITEMS.put(item, intensity);
    }
    
    /**
     * Register radioactive blocks/items by mod ID and name
     * @param modId The mod ID (e.g., "mekanism")
     * @param blockName The block name (e.g., "uranium_ore")
     * @param intensity Radiation intensity
     */
    public static void registerRadioactiveBlockByName(String modId, String blockName, double intensity) {
        ResourceLocation blockId = ResourceLocation.fromNamespaceAndPath(modId, blockName);
        Block block = BuiltInRegistries.BLOCK.get(blockId);
        if (block != null) {
            registerRadioactiveBlock(block, intensity);
        }
    }
    
    public static void registerRadioactiveItemByName(String modId, String itemName, double intensity) {
        String fullName = modId + ":" + itemName;
        registerRadioactiveSourceByName(fullName, intensity);
        
        // Also try direct registration if the item exists
        ResourceLocation itemId = ResourceLocation.fromNamespaceAndPath(modId, itemName);
        Item item = BuiltInRegistries.ITEM.get(itemId);
        if (item != null && !item.equals(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "air")))) {
            registerRadioactiveItem(item, intensity);
        }
    }
    
    /**
     * Get radiation intensity for an ItemStack
     * @param stack The ItemStack to check
     * @return Radiation intensity, or 0 if not radioactive
     */
    public static double getRadiationLevel(ItemStack stack) {
        if (stack.isEmpty()) return 0.0;
        
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        
        // Check direct ResourceLocation registration
        if (RADIOACTIVE_SOURCES.containsKey(itemId)) {
            double radiation = RADIOACTIVE_SOURCES.get(itemId);
            FlamesRadiology.LOGGER.debug("Found radiation for {}: {} (ResourceLocation)", itemId, radiation);
            return radiation;
        }
        
        // Check by string name (for mod compatibility)
        String itemName = itemId.toString();
        if (MOD_RADIOACTIVE_SOURCES.containsKey(itemName)) {
            double radiation = MOD_RADIOACTIVE_SOURCES.get(itemName);
            FlamesRadiology.LOGGER.debug("Found radiation for {}: {} (String)", itemName, radiation);
            return radiation;
        }
        
        // Fallback to direct item registration
        double fallbackRadiation = RADIOACTIVE_ITEMS.getOrDefault(stack.getItem(), 0.0);
        if (fallbackRadiation > 0) {
            FlamesRadiology.LOGGER.debug("Found radiation for {}: {} (Direct)", itemId, fallbackRadiation);
        }
        return fallbackRadiation;
    }
    
    /**
     * Get radiation intensity for a block
     * @param block The block to check
     * @return Radiation intensity, or 0 if not radioactive
     */
    public static double getBlockRadiation(Block block) {
        // First check direct block registration
        if (RADIOACTIVE_BLOCKS.containsKey(block)) {
            return RADIOACTIVE_BLOCKS.get(block);
        }
        
        // Then check string-based registration
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
        String blockName = blockId.toString();
        if (MOD_RADIOACTIVE_SOURCES.containsKey(blockName)) {
            double radiation = MOD_RADIOACTIVE_SOURCES.get(blockName);
            FlamesRadiology.LOGGER.debug("Found block radiation for {}: {}", blockName, radiation);
            return radiation;
        }
        
        return 0.0;
    }
    
    /**
     * Get radiation intensity for an item
     * @param item The item to check
     * @return Radiation intensity, or 0 if not radioactive
     */
    public static double getItemRadiation(Item item) {
        return RADIOACTIVE_ITEMS.getOrDefault(item, 0.0);
    }
    
    /**
     * Check if an ItemStack is radioactive
     * @param stack The ItemStack to check
     * @return true if radioactive
     */
    public static boolean isRadioactive(ItemStack stack) {
        return getRadiationLevel(stack) > 0.0;
    }
    
    /**
     * Check if a block is radioactive
     */
    public static boolean isBlockRadioactive(Block block) {
        return getBlockRadiation(block) > 0.0;
    }
    
    /**
     * Check if an item is radioactive
     */
    public static boolean isItemRadioactive(Item item) {
        return RADIOACTIVE_ITEMS.containsKey(item);
    }
    
    /**
     * Register radioactive sources for a specific mod
     * @param modId The mod ID to register sources for
     */
    public static void registerModSources(String modId) {
        switch (modId.toLowerCase()) {
            case "mekanism":
                registerRadioactiveSourceByName("mekanism:uranium_ore", 0.8);
                registerRadioactiveSourceByName("mekanism:deepslate_uranium_ore", 0.8);
                registerRadioactiveSourceByName("mekanism:raw_uranium", 1.0);
                registerRadioactiveSourceByName("mekanism:ingot_uranium", 2.0);
                registerRadioactiveSourceByName("mekanism:nugget_uranium", 0.55);
                registerRadioactiveSourceByName("mekanism:block_uranium", 5.0);
                registerRadioactiveSourceByName("mekanism:block_raw_uranium", 5.0);
                registerRadioactiveSourceByName("mekanism:shard_uranium", 0.35);
                registerRadioactiveSourceByName("mekanism:crystal_uranium", 0.35);
                registerRadioactiveSourceByName("mekanism:dust_uranium", 0.35);
                registerRadioactiveSourceByName("mekanism:dirty_dust_uranium", 0.35);
                registerRadioactiveSourceByName("mekanism:clump_uranium", 0.35);
                registerRadioactiveSourceByName("mekanism:yellow_cake_uranium", 6.2);
                registerRadioactiveSourceByName("mekanism:pellet_plutonium", 8.0);
                registerRadioactiveSourceByName("mekanism:pellet_polonium", 12.0);
                break;
            case "powah":
                registerRadioactiveSourceByName("powah:uraninite_ore_poor", 0.4);
                registerRadioactiveSourceByName("powah:uraninite_ore", 0.8);
                registerRadioactiveSourceByName("powah:uraninite_ore_dense", 1.0);
                registerRadioactiveSourceByName("powah:deepslate_uraninite_ore_poor", 0.4);
                registerRadioactiveSourceByName("powah:deepslate_uraninite_ore", 0.8);
                registerRadioactiveSourceByName("powah:deepslate_uraninite_ore_dense", 1.0);
                registerRadioactiveSourceByName("powah:uraninite_raw_poor", 0.5);
                registerRadioactiveSourceByName("powah:uraninite_raw", 0.9);
                registerRadioactiveSourceByName("powah:uraninite_raw_dense", 1.2);
                registerRadioactiveSourceByName("powah:uraninite", 2.5);
                registerRadioactiveSourceByName("powah:uraninite_block", 5.0);
                break;
            case "immersiveengineering":
                registerRadioactiveSourceByName("immersiveengineering:ore_uranium", 0.8);
                registerRadioactiveSourceByName("immersiveengineering:deepslate_ore_uranium", 0.8);
                registerRadioactiveSourceByName("immersiveengineering:raw_uranium", 1.0);
                registerRadioactiveSourceByName("immersiveengineering:ingot_uranium", 2.0);
                registerRadioactiveSourceByName("immersiveengineering:nugget_uranium", 0.35);
                registerRadioactiveSourceByName("immersiveengineering:storage_uranium", 5.0);
                registerRadioactiveSourceByName("immersiveengineering:slab_storage_uranium", 2.5);
                registerRadioactiveSourceByName("immersiveengineering:raw_block_uranium", 5.0);
                registerRadioactiveSourceByName("immersiveengineering:plate_uranium", 0.35);
                registerRadioactiveSourceByName("immersiveengineering:dust_uranium", 0.35);
                break;
            case "biggerreactors":
                registerRadioactiveBlockByName("biggerreactors", "uranium_ore", 13.0);
                registerRadioactiveBlockByName("biggerreactors", "uranium_block", 42.0);
                registerRadioactiveItemByName("biggerreactors", "uranium_ingot", 7.5);
                break;
            case "nuclearcraft":
                registerRadioactiveBlockByName("nuclearcraft", "uranium_ore", 16.0);
                registerRadioactiveBlockByName("nuclearcraft", "uranium_block", 48.0);
                registerRadioactiveItemByName("nuclearcraft", "uranium", 8.5);
                break;
            case "ic2":
                registerRadioactiveBlockByName("ic2", "uranium_ore", 14.0);
                registerRadioactiveBlockByName("ic2", "uranium_block", 46.0);
                registerRadioactiveItemByName("ic2", "uranium", 8.0);
                break;
        }
    }

    /**
     * Initialize default radioactive sources from popular mods
     */
    public static void initializeDefaultSources() {
        registerFlamesRadiologySources();
        registerMekanismSources();
        registerImmersiveEngineeringSources();
        registerPowahSources();
        registerBiggerReactorsSources();
        registerNuclearCraftSources();
        registerIC2Sources();
    }
    
    private static void registerFlamesRadiologySources() {
        // Flames Radiology mod's own radioactive materials
        // Register contaminated water block directly for reliable detection
        registerRadioactiveBlock(ModBlocks.CONTAMINATED_WATER_BLOCK.get(), 0.05);
        
        // Also register by name for compatibility
        registerRadioactiveSourceByName("flames_radiology:contaminated_water_block",0.05);
        registerRadioactiveSourceByName("flames_radiology:contaminated_water_bucket", 0.05);
        
        // Register the bucket item directly
        registerRadioactiveItem(ModItems.CONTAMINATED_WATER_BUCKET.get(), 0.05);
        
        // Register contaminated blocks
        registerRadioactiveBlock(ModBlocks.WASTE_LAND_DIRT.get(), 0.05);
        registerRadioactiveBlock(ModBlocks.CONTAMINATED_GRASS_BLOCK.get(), 0.05);
        
        // Also register by name for compatibility
        registerRadioactiveSourceByName("flames_radiology:waste_land_dirt", 0.05);
        registerRadioactiveSourceByName("flames_radiology:contaminated_grass_block", 0.05);
    }
    private static void registerMekanismSources() {
        // Mekanism radioactive materials
        registerRadioactiveSourceByName("mekanism:uranium_ore", 0.8);
        registerRadioactiveSourceByName("mekanism:deepslate_uranium_ore", 0.8);
        registerRadioactiveSourceByName("mekanism:raw_uranium", 1.0);
        registerRadioactiveSourceByName("mekanism:ingot_uranium", 2.0);
        registerRadioactiveSourceByName("mekanism:nugget_uranium", 0.55);
        registerRadioactiveSourceByName("mekanism:block_uranium", 5.0);
        registerRadioactiveSourceByName("mekanism:block_raw_uranium", 5.0);
        registerRadioactiveSourceByName("mekanism:shard_uranium", 0.35);
        registerRadioactiveSourceByName("mekanism:crystal_uranium", 0.35);
        registerRadioactiveSourceByName("mekanism:dust_uranium", 0.35);
        registerRadioactiveSourceByName("mekanism:dirty_dust_uranium", 0.35);
        registerRadioactiveSourceByName("mekanism:clump_uranium", 0.35);
        
        // Enriched materials
        registerRadioactiveSourceByName("mekanism:yellow_cake_uranium", 6.2);
        registerRadioactiveSourceByName("mekanism:pellet_plutonium", 8.0);
        registerRadioactiveSourceByName("mekanism:pellet_polonium", 12.0);
    }
    
    private static void registerImmersiveEngineeringSources() {
        // Immersive Engineering radioactive materials
        registerRadioactiveSourceByName("immersiveengineering:ore_uranium", 0.8);
        registerRadioactiveSourceByName("immersiveengineering:deepslate_ore_uranium", 0.8);
        registerRadioactiveSourceByName("immersiveengineering:raw_uranium", 1.0);
        registerRadioactiveSourceByName("immersiveengineering:ingot_uranium", 2.0);
        registerRadioactiveSourceByName("immersiveengineering:nugget_uranium", 0.35);
        registerRadioactiveSourceByName("immersiveengineering:storage_uranium", 5.0);
        registerRadioactiveSourceByName("immersiveengineering:slab_storage_uranium", 2.5);
        registerRadioactiveSourceByName("immersiveengineering:raw_block_uranium", 5.0);
        registerRadioactiveSourceByName("immersiveengineering:plate_uranium", 0.35);
        registerRadioactiveSourceByName("immersiveengineering:dust_uranium", 0.35);
    }
    
    private static void registerPowahSources() {
        // Powah radioactive materials
        registerRadioactiveSourceByName("powah:uraninite_ore_poor", 0.4);
        registerRadioactiveSourceByName("powah:uraninite_ore", 0.8);
        registerRadioactiveSourceByName("powah:uraninite_ore_dense", 1.0);
        registerRadioactiveSourceByName("powah:deepslate_uraninite_ore_poor", 0.4);
        registerRadioactiveSourceByName("powah:deepslate_uraninite_ore", 0.8);
        registerRadioactiveSourceByName("powah:deepslate_uraninite_ore_dense", 1.0);
        registerRadioactiveSourceByName("powah:uraninite_raw_poor", 0.5);
        registerRadioactiveSourceByName("powah:uraninite_raw", 0.9);
        registerRadioactiveSourceByName("powah:uraninite_raw_dense", 1.2);
        registerRadioactiveSourceByName("powah:uraninite", 2.5);
        registerRadioactiveSourceByName("powah:uraninite_block", 5.0);
    }
    
    private static void registerBiggerReactorsSources() {
        // BiggerReactors (Extreme Reactors)
        registerRadioactiveSourceByName("biggerreactors:uranium_ore", 0.8);
        registerRadioactiveSourceByName("biggerreactors:uranium_block", 5.0);
        registerRadioactiveSourceByName("biggerreactors:uranium_ingot", 2.0);
    }
    
    private static void registerNuclearCraftSources() {
        // NuclearCraft uranium
        registerRadioactiveSourceByName("nuclearcraft:uranium_ore", 0.8);
        registerRadioactiveSourceByName("nuclearcraft:uranium_block", 5.0);
        registerRadioactiveSourceByName("nuclearcraft:uranium", 2.0);
    }
    
    private static void registerIC2Sources() {
        // IC2 uranium (if present)
        registerRadioactiveSourceByName("ic2:uranium_ore", 0.8);
        registerRadioactiveSourceByName("ic2:uranium_block", 5.0);
        registerRadioactiveSourceByName("ic2:uranium", 2.0);
    }
    
    /**
     * Get all registered radioactive blocks
     */
    public static Map<Block, Double> getAllRadioactiveBlocks() {
        return new HashMap<>(RADIOACTIVE_BLOCKS);
    }
    
    /**
     * Get all registered radioactive items
     */
    public static Map<Item, Double> getAllRadioactiveItems() {
        return new HashMap<>(RADIOACTIVE_ITEMS);
    }
}
