package net.flame.flamesradiology.radiation;

import net.flame.flamesradiology.FlamesRadiology;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public class LeadShielding {
    
    // Set of lead blocks from various mods
    private static final Set<String> LEAD_BLOCKS = new HashSet<>();
    
    static {
        // Initialize lead blocks from various mods
        initializeLeadBlocks();
    }
    
    private static void initializeLeadBlocks() {
        // Flames Radiology lead blocks
        LEAD_BLOCKS.add("flames_radiology:lead_block");
        
        // Mekanism lead blocks
        LEAD_BLOCKS.add("mekanism:block_lead");
        
        // Immersive Engineering lead blocks
        LEAD_BLOCKS.add("immersiveengineering:storage_lead");
        
        // Thermal Foundation lead blocks
        LEAD_BLOCKS.add("thermal:lead_block");
        
        // Create lead blocks
        LEAD_BLOCKS.add("create:lead_block");
        
        // Industrial Foregoing lead blocks
        LEAD_BLOCKS.add("industrialforegoing:lead_block");
        
        // Tech Reborn lead blocks
        LEAD_BLOCKS.add("techreborn:lead_storage_block");
        
        // Applied Energistics lead blocks
        LEAD_BLOCKS.add("appliedenergistics2:lead_block");
        
        // Tinkers' Construct lead blocks
        LEAD_BLOCKS.add("tconstruct:lead_block");
        
        // Emendatus Enigmatica lead blocks
        LEAD_BLOCKS.add("emendatusenigmatica:lead_block");
    }
    
    /**
     * Check if a block is a lead block
     */
    public static boolean isLeadBlock(Block block) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
        return LEAD_BLOCKS.contains(blockId.toString());
    }
    
    /**
     * Check if a block at a position is a lead block
     */
    public static boolean isLeadBlock(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return isLeadBlock(state.getBlock());
    }
    
    /**
     * Calculate radiation shielding between a radioactive source and a player
     * Uses ray-casting to count lead blocks in the path
     * 
     * @param level The world level
     * @param sourcePos Position of the radioactive source
     * @param playerPos Position of the player
     * @param originalRadiation The original radiation level without shielding
     * @return The radiation level after lead shielding is applied
     */
    public static double calculateShieldedRadiation(Level level, BlockPos sourcePos, Vec3 playerPos, double originalRadiation) {
        if (originalRadiation <= 0) {
            return 0.0;
        }
        
        // Check if source is completely enclosed by lead blocks
        if (isCompletelyEnclosed(level, sourcePos)) {
            return 0.0; // Complete shielding
        }
        
        // Calculate ray from source to player
        Vec3 sourceVec = Vec3.atCenterOf(sourcePos);
        Vec3 direction = playerPos.subtract(sourceVec).normalize();
        double distance = sourceVec.distanceTo(playerPos);
        
        // Count lead blocks along the ray
        int leadBlockCount = countLeadBlocksInPath(level, sourceVec, direction, distance);
        
        // Calculate shielding factor
        // Each lead block reduces radiation by 50% (configurable)
        double shieldingFactor = Math.pow(0.5, leadBlockCount);
        
        return originalRadiation * shieldingFactor;
    }
    
    /**
     * Check if a radioactive source is completely enclosed by lead blocks
     */
    private static boolean isCompletelyEnclosed(Level level, BlockPos sourcePos) {
        // Check all 6 adjacent faces
        BlockPos[] adjacentPositions = {
            sourcePos.above(),    // Top
            sourcePos.below(),    // Bottom
            sourcePos.north(),    // North
            sourcePos.south(),    // South
            sourcePos.east(),     // East
            sourcePos.west()      // West
        };
        
        for (BlockPos pos : adjacentPositions) {
            BlockState state = level.getBlockState(pos);
            if (!isLeadBlock(state.getBlock())) {
                FlamesRadiology.LOGGER.debug("Source at {} not completely enclosed - missing lead at {}, found: {}", 
                    sourcePos, pos, BuiltInRegistries.BLOCK.getKey(state.getBlock()));
                return false; // Not completely enclosed
            }
        }
        
        FlamesRadiology.LOGGER.debug("Source at {} is completely enclosed by lead blocks - blocking all radiation", sourcePos);
        return true; // Completely enclosed
    }
    
    /**
     * Count lead blocks along a ray path using DDA-like algorithm
     */
    private static int countLeadBlocksInPath(Level level, Vec3 start, Vec3 direction, double maxDistance) {
        int leadCount = 0;
        
        // Step size for ray marching (smaller = more accurate, but slower)
        double stepSize = 0.5;
        int maxSteps = (int) Math.ceil(maxDistance / stepSize);
        
        for (int step = 1; step <= maxSteps; step++) {
            Vec3 currentPos = start.add(direction.scale(step * stepSize));
            BlockPos blockPos = BlockPos.containing(currentPos);
            
            // Skip if we've reached the end
            if (start.distanceTo(currentPos) >= maxDistance) {
                break;
            }
            
            // Check if current block is lead
            if (isLeadBlock(level, blockPos)) {
                leadCount++;
            }
        }
        
        return leadCount;
    }
    
    /**
     * Get the shielding effectiveness of lead blocks
     * @param leadBlockCount Number of lead blocks between source and player
     * @return Shielding factor (0.0 = complete shielding, 1.0 = no shielding)
     */
    public static double getShieldingFactor(int leadBlockCount) {
        // Each lead block reduces radiation by 50%
        return Math.pow(0.5, leadBlockCount);
    }
    
    /**
     * Add a custom lead block to the registry
     */
    public static void addLeadBlock(String blockId) {
        LEAD_BLOCKS.add(blockId);
    }
    
    /**
     * Remove a lead block from the registry
     */
    public static void removeLeadBlock(String blockId) {
        LEAD_BLOCKS.remove(blockId);
    }
    
    /**
     * Get all registered lead blocks
     */
    public static Set<String> getAllLeadBlocks() {
        return new HashSet<>(LEAD_BLOCKS);
    }
}
