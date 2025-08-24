package net.flame.flamesradiology.radiation;

import net.flame.flamesradiology.registry.RadioactiveSourceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages radiation calculations and source detection using inverse square law
 */
public class RadiationManager {
    private static final int MAX_SCAN_RADIUS = 16;
    private static final double MIN_RADIATION_THRESHOLD = 0.1;
    
    /**
     * Calculate total radiation level at a position from all nearby sources
     * @param level The world level
     * @param position Position to calculate radiation at
     * @return Total radiation level
     */
    public static double calculateRadiationAt(Level level, BlockPos position) {
        List<RadiationSource> sources = findRadiationSources(level, position, MAX_SCAN_RADIUS);
        double totalRadiation = 0.0;
        
        for (RadiationSource source : sources) {
            totalRadiation += source.getRadiationAt(position);
        }
        
        return totalRadiation;
    }
    
    /**
     * Find all radiation sources within range of a position
     * @param level The world level
     * @param centerPos Center position to scan from
     * @param radius Scan radius
     * @return List of radiation sources
     */
    public static List<RadiationSource> findRadiationSources(Level level, BlockPos centerPos, int radius) {
        List<RadiationSource> sources = new ArrayList<>();
        
        // Scan for radioactive blocks
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = centerPos.offset(x, y, z);
                    BlockState blockState = level.getBlockState(checkPos);
                    
                    double blockRadiation = RadioactiveSourceRegistry.getBlockRadiation(blockState.getBlock());
                    if (blockRadiation > 0) {
                        RadiationSourceType type = RadiationSourceType.getBlockType(blockRadiation);
                        sources.add(new RadiationSource(checkPos, blockRadiation, type));
                    }
                    
                    // Check containers for radioactive items
                    BlockEntity blockEntity = level.getBlockEntity(checkPos);
                    if (blockEntity instanceof Container container) {
                        double containerRadiation = calculateContainerRadiation(container);
                        if (containerRadiation > 0) {
                            sources.add(new RadiationSource(checkPos, containerRadiation, RadiationSourceType.CONTAINER_STORED));
                        }
                    }
                }
            }
        }
        
        // Scan for radioactive items on ground
        AABB searchArea = new AABB(
            centerPos.getX() - radius, centerPos.getY() - radius, centerPos.getZ() - radius,
            centerPos.getX() + radius, centerPos.getY() + radius, centerPos.getZ() + radius
        );
        
        List<ItemEntity> itemEntities = level.getEntitiesOfClass(ItemEntity.class, searchArea);
        for (ItemEntity itemEntity : itemEntities) {
            ItemStack stack = itemEntity.getItem();
            double itemRadiation = RadioactiveSourceRegistry.getRadiationLevel(stack);
            if (itemRadiation > 0) {
                // Scale by stack size
                double totalItemRadiation = itemRadiation * Math.log(stack.getCount() + 1);
                RadiationSourceType type = RadiationSourceType.getItemType(itemRadiation);
                sources.add(new RadiationSource(itemEntity.blockPosition(), totalItemRadiation, RadiationSourceType.GROUND_ITEM));
            }
        }
        
        return sources;
    }
    
    /**
     * Calculate radiation from player's inventory
     * @param player The player
     * @return Radiation level from inventory
     */
    public static double calculateInventoryRadiation(Player player) {
        double totalRadiation = 0.0;
        
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            double itemRadiation = RadioactiveSourceRegistry.getRadiationLevel(stack);
            if (itemRadiation > 0) {
                // Inventory items have reduced radiation due to being carried
                totalRadiation += itemRadiation * Math.log(stack.getCount() + 1) * 0.5;
            }
        }
        
        return totalRadiation;
    }
    
    /**
     * Calculate radiation from items in a container
     * @param container The container to check
     * @return Total radiation from container contents
     */
    private static double calculateContainerRadiation(Container container) {
        double totalRadiation = 0.0;
        
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            double itemRadiation = RadioactiveSourceRegistry.getRadiationLevel(stack);
            if (itemRadiation > 0) {
                // Container storage provides some shielding
                totalRadiation += itemRadiation * Math.log(stack.getCount() + 1) * 0.7;
            }
        }
        
        return totalRadiation;
    }
    
    /**
     * Get total radiation level affecting a player
     * Combines environmental and inventory radiation
     * @param level The world level
     * @param player The player
     * @return Total radiation level in mSv/t
     */
    public static double getTotalRadiation(Level level, Player player) {
        BlockPos playerPos = player.blockPosition();
        
        // Environmental radiation
        double environmentalRadiation = calculateRadiationAt(level, playerPos);
        
        // Inventory radiation
        double inventoryRadiation = calculateInventoryRadiation(player);
        
        // Combine with different weights
        return environmentalRadiation + (inventoryRadiation * 0.3);
    }
    
    /**
     * Get radiation level for Geiger Counter detection
     * Combines environmental and inventory radiation
     * @param level The world level
     * @param player The player
     * @return Detection count for Geiger Counter
     */
    public static int getGeigerCounterReading(Level level, Player player) {
        double totalRadiation = getTotalRadiation(level, player);
        
        // Convert to Geiger Counter reading (logarithmic scale for better gameplay)
        return (int) Math.max(0, Math.log(totalRadiation + 1) * 10);
    }
    
    /**
     * Check if a position has dangerous radiation levels
     * @param level The world level
     * @param position Position to check
     * @return true if radiation is above danger threshold
     */
    public static boolean isDangerousRadiation(Level level, BlockPos position) {
        return calculateRadiationAt(level, position) > 50.0;
    }
    
    /**
     * Get radiation sources affecting a specific position
     * @param level The world level
     * @param position Position to check
     * @return List of sources that significantly affect this position
     */
    public static List<RadiationSource> getAffectingSources(Level level, BlockPos position) {
        List<RadiationSource> allSources = findRadiationSources(level, position, MAX_SCAN_RADIUS);
        List<RadiationSource> affectingSources = new ArrayList<>();
        
        for (RadiationSource source : allSources) {
            if (source.affectsPosition(position, MAX_SCAN_RADIUS)) {
                affectingSources.add(source);
            }
        }
        
        return affectingSources;
    }
}
