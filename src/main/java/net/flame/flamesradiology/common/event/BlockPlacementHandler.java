package net.flame.flamesradiology.common.event;

import net.flame.flamesradiology.FlamesRadiology;
import net.flame.flamesradiology.registry.RadioactiveSourceRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = FlamesRadiology.MOD_ID)
public class BlockPlacementHandler {
    
    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        LevelAccessor levelAccessor = event.getLevel();
        
        // Only process on server side and ensure it's a Level instance
        if (!(levelAccessor instanceof Level level) || level.isClientSide()) {
            return;
        }
        
        BlockPos pos = event.getPos();
        BlockState placedState = event.getPlacedBlock();
        Block placedBlock = placedState.getBlock();
        
        // Check if the placed block is radioactive
        if (RadioactiveSourceRegistry.isBlockRadioactive(placedBlock)) {
            double radiation = RadioactiveSourceRegistry.getBlockRadiation(placedBlock);
            
            FlamesRadiology.LOGGER.debug("Radioactive block placed at {}: {} (radiation: {})", 
                pos, placedBlock.getDescriptionId(), radiation);
            
            // Trigger immediate contamination check for nearby blocks
            BlockContaminationHandler.checkImmediateContamination(level, pos);
        }
    }
}
