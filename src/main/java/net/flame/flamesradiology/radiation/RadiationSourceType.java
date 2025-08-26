package net.flame.flamesradiology.radiation;

/**
 * Types of radiation sources with different characteristics
 */
public enum RadiationSourceType {
    /**
     * Ore blocks - moderate radiation with standard range
     */
    ORE_BLOCK(1.0),
    
    /**
     * Refined blocks (uranium blocks, etc.) - higher intensity
     */
    REFINED_BLOCK(1.5),
    
    /**
     * Raw items - lower intensity, shorter range
     */
    RAW_ITEM(0.7),
    
    /**
     * Processed items (ingots, etc.) - moderate intensity
     */
    PROCESSED_ITEM(0.8),
    
    /**
     * Highly enriched materials - very high intensity
     */
    ENRICHED_MATERIAL(2.0),
    
    /**
     * Container with radioactive materials - reduced intensity due to shielding
     */
    CONTAINER_STORED(0.6),
    
    /**
     * Ground items - slightly reduced due to dispersion
     */
    GROUND_ITEM(0.9);
    
    private final double modifier;
    
    RadiationSourceType(double modifier) {
        this.modifier = modifier;
    }
    
    public double getModifier() {
        return modifier;
    }
    
    /**
     * Get appropriate type for a block-based source
     */
    public static RadiationSourceType getBlockType(double intensity) {
        if (intensity >= 40.0) {
            return REFINED_BLOCK;
        } else if (intensity >= 15.0) {
            return ORE_BLOCK;
        } else {
            return ORE_BLOCK; // Default for lower intensity blocks
        }
    }
    
    /**
     * Get appropriate type for an item-based source
     */
    public static RadiationSourceType getItemType(double intensity) {
        if (intensity >= 8.0) {
            return PROCESSED_ITEM;
        } else if (intensity >= 5.0) {
            return RAW_ITEM;
        } else {
            return RAW_ITEM; // Default for lower intensity items
        }
    }
}
