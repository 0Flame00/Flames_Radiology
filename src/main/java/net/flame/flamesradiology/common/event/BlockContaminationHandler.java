package net.flame.flamesradiology.common.event;

import net.flame.flamesradiology.FlamesRadiology;
import net.flame.flamesradiology.common.init.ModBlocks;
import net.flame.flamesradiology.registry.RadioactiveSourceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Random;

@EventBusSubscriber(modid = FlamesRadiology.MOD_ID)
public class BlockContaminationHandler {
    
    private static final Random RANDOM = new Random();
    private static final int CONTAMINATION_CHANCE = 20; // 1 in 20 chance per tick
    
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        // Get the overworld server level
        ServerLevel overworld = event.getServer().getLevel(Level.OVERWORLD);
        if (overworld == null) {
            return;
        }
        
        // Process contamination spread at moderate frequency for balanced gameplay
        if (overworld.getGameTime() % 40 != 0) { // Every 2 seconds (40 ticks)
            return;
        }
        
        // Add debug logging to see if this is being called
        FlamesRadiology.LOGGER.debug("Processing contamination spread tick at game time: {}", overworld.getGameTime());
        
        // Get all players and scan around them for contamination opportunities
        overworld.players().forEach(player -> {
            BlockPos playerPos = player.blockPosition();
            FlamesRadiology.LOGGER.debug("Scanning for contamination around player at: {}", playerPos);
            scanForContamination(overworld, playerPos, 8); // 8 block radius around players (reduced from 16)
        });
    }
    
    private static void scanForContamination(ServerLevel level, BlockPos centerPos, int radius) {
        // Find all radioactive and contaminated blocks in the area
        for (int x = centerPos.getX() - radius; x <= centerPos.getX() + radius; x++) {
            for (int y = centerPos.getY() - radius/2; y <= centerPos.getY() + radius/2; y++) {
                for (int z = centerPos.getZ() - radius; z <= centerPos.getZ() + radius; z++) {
                    BlockPos sourcePos = new BlockPos(x, y, z);
                    
                    if (!level.isLoaded(sourcePos)) {
                        continue;
                    }
                    
                    BlockState sourceState = level.getBlockState(sourcePos);
                    Block sourceBlock = sourceState.getBlock();
                    
                    // Check if this block is radioactive or contaminated (can spread contamination)
                    if (RadioactiveSourceRegistry.isBlockRadioactive(sourceBlock) || isContaminatedBlock(sourceBlock)) {
                        // Check all 6 adjacent blocks for contamination
                        for (Direction direction : Direction.values()) {
                            BlockPos adjacentPos = sourcePos.relative(direction);
                            
                            if (!level.isLoaded(adjacentPos)) {
                                continue;
                            }
                            
                            BlockState adjacentState = level.getBlockState(adjacentPos);
                            Block adjacentBlock = adjacentState.getBlock();
                            
                            // Check if this adjacent block can be contaminated
                            if (canBlockBeContaminated(adjacentBlock)) {
                                FlamesRadiology.LOGGER.debug("Found contaminatable block adjacent to radioactive source at {}: {}", adjacentPos, adjacentBlock.getDescriptionId());
                                
                                // Random chance for contamination to occur
                                if (RANDOM.nextInt(CONTAMINATION_CHANCE) == 0) {
                                    contaminateBlock(level, adjacentPos, adjacentState);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private static boolean canBlockBeContaminated(Block block) {
        return block == Blocks.DIRT || 
               block == Blocks.GRASS_BLOCK || 
               block == Blocks.WATER;
    }
    
    private static boolean isContaminatedBlock(Block block) {
        return block == ModBlocks.WASTE_LAND_DIRT.get() ||
               block == ModBlocks.CONTAMINATED_GRASS_BLOCK.get() ||
               block == ModBlocks.CONTAMINATED_WATER_BLOCK.get();
    }
    
    
    private static void contaminateBlock(ServerLevel level, BlockPos pos, BlockState originalState) {
        Block originalBlock = originalState.getBlock();
        BlockState newState = null;
        
        // Determine what to convert the block to
        if (originalBlock == Blocks.DIRT) {
            newState = ModBlocks.WASTE_LAND_DIRT.get().defaultBlockState();
            FlamesRadiology.LOGGER.debug("Converting dirt to wasteland dirt at {}", pos);
        } else if (originalBlock == Blocks.GRASS_BLOCK) {
            newState = ModBlocks.CONTAMINATED_GRASS_BLOCK.get().defaultBlockState();
            FlamesRadiology.LOGGER.debug("Converting grass to contaminated grass at {}", pos);
        } else if (originalBlock == Blocks.WATER) {
            // For water, we need to check if it's a source block
            if (originalState.getFluidState().getType() == Fluids.WATER && 
                originalState.getFluidState().isSource()) {
                newState = ModBlocks.CONTAMINATED_WATER_BLOCK.get().defaultBlockState();
                FlamesRadiology.LOGGER.debug("Converting water to contaminated water at {}", pos);
            }
        }
        
        // Apply the contamination
        if (newState != null) {
            level.setBlock(pos, newState, Block.UPDATE_ALL);
            
            // Add some visual effects
            spawnContaminationEffects(level, pos);
            
            FlamesRadiology.LOGGER.info("Contaminated block at {} - {} -> {}", 
                pos, originalBlock.getDescriptionId(), newState.getBlock().getDescriptionId());
        }
    }
    
    private static void spawnContaminationEffects(ServerLevel level, BlockPos pos) {
        // Spawn some particles to indicate contamination
        // This will create a small puff of smoke-like particles
        for (int i = 0; i < 5; i++) {
            double x = pos.getX() + 0.5 + (RANDOM.nextDouble() - 0.5) * 0.8;
            double y = pos.getY() + 0.5 + (RANDOM.nextDouble() - 0.5) * 0.8;
            double z = pos.getZ() + 0.5 + (RANDOM.nextDouble() - 0.5) * 0.8;
            
            level.sendParticles(
                net.minecraft.core.particles.ParticleTypes.SMOKE,
                x, y, z,
                1, // particle count
                0.0, 0.1, 0.0, // velocity
                0.02 // speed
            );
        }
    }
    
    /**
     * Check if a block at the given position should be contaminated immediately
     * This is called when radioactive blocks are placed
     */
    public static void checkImmediateContamination(Level level, BlockPos radioactivePos) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        
        // Check all adjacent blocks for immediate contamination
        for (Direction direction : Direction.values()) {
            BlockPos adjacentPos = radioactivePos.relative(direction);
            
            if (!level.isLoaded(adjacentPos)) {
                continue;
            }
            
            BlockState adjacentState = level.getBlockState(adjacentPos);
            Block adjacentBlock = adjacentState.getBlock();
            
            if (canBlockBeContaminated(adjacentBlock)) {
                // Higher chance for immediate contamination when radioactive block is placed
                if (RANDOM.nextInt(10) == 0) { // 10% chance
                    contaminateBlock(serverLevel, adjacentPos, adjacentState);
                }
            }
        }
    }
}
