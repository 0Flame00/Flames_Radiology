package net.flame.flamesradiology.common.event;

import net.flame.flamesradiology.common.init.ModItems;
import net.flame.flamesradiology.common.util.GasFilterManager;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class GasFilterInteractionHandler {
    
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack heldItem = event.getItemStack();
        
        // Check if player is holding a gas filter
        if (heldItem.getItem() == ModItems.GAS_FILTER.get()) {
            ItemStack helmet = player.getInventory().getArmor(3); // Helmet slot
            
            // Check if player is wearing a compatible helmet
            if (GasFilterManager.isGasFilterHelmet(helmet)) {
                // Replenish gas filter
                if (GasFilterManager.replenishGasFilter(player, helmet)) {
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }
            }
        }
        
        // Check if player is holding a helmet and right-clicking with gas filter in inventory
        if (GasFilterManager.isGasFilterHelmet(heldItem)) {
            if (GasFilterManager.replenishGasFilter(player, heldItem)) {
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
}
