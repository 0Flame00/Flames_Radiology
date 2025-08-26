package net.flame.flamesradiology.common.event;

import net.flame.flamesradiology.common.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = "flames_radiology")
public class AxeStrippingHandler {
    
    private static final Map<Block, Block> STRIPPING_MAP = new HashMap<>();
    
    public static void initStrippingMap() {
        // Register stripping conversions
        STRIPPING_MAP.put(ModBlocks.DEAD_LOG.get(), ModBlocks.STRIPPED_DEAD_LOG.get());
        STRIPPING_MAP.put(ModBlocks.DEAD_WOOD.get(), ModBlocks.STRIPPED_DEAD_WOOD.get());
    }
    
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        ItemStack itemStack = event.getItemStack();
        
        // Check if player is using an axe
        if (!(itemStack.getItem() instanceof AxeItem)) {
            return;
        }
        
        BlockState blockState = level.getBlockState(pos);
        Block strippedBlock = STRIPPING_MAP.get(blockState.getBlock());
        
        if (strippedBlock != null) {
            // Create the stripped block state with the same properties (axis rotation)
            BlockState strippedState = strippedBlock.defaultBlockState();
            if (blockState.hasProperty(net.minecraft.world.level.block.RotatedPillarBlock.AXIS)) {
                strippedState = strippedState.setValue(
                    net.minecraft.world.level.block.RotatedPillarBlock.AXIS,
                    blockState.getValue(net.minecraft.world.level.block.RotatedPillarBlock.AXIS)
                );
            }
            
            // Set the new block state
            level.setBlock(pos, strippedState, 11);
            
            // Play stripping sound
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            
            // Damage the axe
            if (!player.getAbilities().instabuild) {
                itemStack.hurtAndBreak(1, player, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
            }
            
            event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
            event.setCanceled(true);
        }
    }
}
