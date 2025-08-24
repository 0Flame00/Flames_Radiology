package net.flame.flamesradiology.common.event;

import net.flame.flamesradiology.common.init.ModEffects;
import net.flame.flamesradiology.radiation.ExposureManager;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

/**
 * Handles player death events to clear radiation effects and reset exposure
 */
@EventBusSubscriber
public class PlayerDeathHandler {
    
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        
        // Clear all radiation effects
        player.removeEffect(ModEffects.RADIATION_SICKNESS);
        player.removeEffect(ModEffects.RADIATION_POISONING);
        player.removeEffect(ModEffects.ACUTE_RADIATION_SYNDROME);
        
        // Reset radiation exposure to zero
        ExposureManager.clearPlayerExposure(player);
        
        // Send confirmation message (optional debug)
        player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§2Radiation exposure cleared due to death"));
    }
}
