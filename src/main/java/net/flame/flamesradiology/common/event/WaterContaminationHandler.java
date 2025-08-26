package net.flame.flamesradiology.common.event;

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
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber
public class WaterContaminationHandler {
    
    private static int tickCounter = 0;
    private static final int CHECK_INTERVAL = 20; // Check every second (20 ticks)
    
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        
        tickCounter++;
        if (tickCounter < CHECK_INTERVAL) {
            return;
        }
        tickCounter = 0;
        
        // Check for water contamination around radioactive sources
        checkWaterContamination(level);
    }
    
    private static void checkWaterContamination(ServerLevel level) {
        // Check around all players for water contamination to avoid chunk access issues
        for (var player : level.players()) {
            BlockPos playerPos = player.blockPosition();
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            
            // Check in a radius around each player
            int radius = 32; // 2 chunks radius
            for (int x = -radius; x <= radius; x += 4) {
                for (int z = -radius; z <= radius; z += 4) {
                    for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y += 8) {
                        mutablePos.set(playerPos.getX() + x, y, playerPos.getZ() + z);
                        
                        BlockState blockState = level.getBlockState(mutablePos);
                        Block block = blockState.getBlock();
                        
                        // Check if this block is radioactive
                        if (RadioactiveSourceRegistry.isBlockRadioactive(block)) {
                            double radiationLevel = RadioactiveSourceRegistry.getBlockRadiation(block);
                            
                            // Only contaminate if radiation level is significant
                            if (radiationLevel > 0.5) {
                                contaminateNearbyWater(level, mutablePos.immutable(), radiationLevel);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private static void contaminateNearbyWater(ServerLevel level, BlockPos radioactivePos, double radiationLevel) {
        // Check blocks in a radius around the radioactive source
        int radius = Math.min((int)(radiationLevel * 2), 8); // Max radius of 8 blocks
        
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos checkPos = radioactivePos.offset(dx, dy, dz);
                    BlockState blockState = level.getBlockState(checkPos);
                    
                    // Check if this is a water source block
                    if (blockState.getFluidState().getType() == Fluids.WATER && 
                        blockState.getFluidState().isSource()) {
                        
                        // Calculate distance for contamination probability
                        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                        double contaminationChance = Math.max(0.1, (radiationLevel / 10.0) * (1.0 - distance / radius));
                        
                        // Random chance to contaminate based on radiation level and distance
                        if (level.getRandom().nextDouble() < contaminationChance) {
                            // Replace water with contaminated water
                            if (blockState.is(Blocks.WATER)) {
                                level.setBlock(checkPos, ModBlocks.CONTAMINATED_WATER_BLOCK.get().defaultBlockState(), 3);
                            }
                        }
                    }
                }
            }
        }
    }
}
