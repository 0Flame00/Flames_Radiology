package net.flame.flamesradiology.common.event;

import net.flame.flamesradiology.common.item.GeigerCounterItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class GeigerCounterEventHandler {
    
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        
        // Only run on client side and check every tick for precise timing
        if (!player.level().isClientSide) {
            return;
        }
        
        // Check if player has an enabled Geiger Counter anywhere in their inventory
        ItemStack enabledGeigerCounter = null;
        
        // Check main inventory
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem() instanceof GeigerCounterItem && 
                GeigerCounterItem.isGeigerEnabled(itemStack)) {
                enabledGeigerCounter = itemStack;
                break;
            }
        }
        
        // Check offhand if not found in main inventory
        if (enabledGeigerCounter == null) {
            ItemStack offHandItem = player.getOffhandItem();
            if (offHandItem.getItem() instanceof GeigerCounterItem && 
                GeigerCounterItem.isGeigerEnabled(offHandItem)) {
                enabledGeigerCounter = offHandItem;
            }
        }
        
        // Perform scan if enabled Geiger Counter found
        if (enabledGeigerCounter != null && enabledGeigerCounter.getItem() instanceof GeigerCounterItem geigerCounter) {
            geigerCounter.performAutomaticScan(player.level(), player, enabledGeigerCounter);
        }
    }
}
