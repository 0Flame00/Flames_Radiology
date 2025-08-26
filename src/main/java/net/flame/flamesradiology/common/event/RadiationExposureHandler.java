package net.flame.flamesradiology.common.event;

import net.flame.flamesradiology.common.init.ModEffects;
import net.flame.flamesradiology.common.util.GasFilterManager;
import net.flame.flamesradiology.common.util.RadiationProtection;
import net.flame.flamesradiology.radiation.ExposureManager;
import net.flame.flamesradiology.radiation.RadiationManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class RadiationExposureHandler {
    private static int tickCounter = 0;
    private static final int UPDATE_INTERVAL = 20; // Update every 20 ticks (1 second)
    private static final int DAMAGE_INTERVAL = 40; // Damage every 40 ticks (2 seconds)
    
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
        if (serverPlayer.level().isClientSide) return;
        
        tickCounter++;
        
        // Update exposure every second
        if (tickCounter % UPDATE_INTERVAL == 0) {
            updateRadiationExposure(serverPlayer);
            // Update gas filter durability every second
            GasFilterManager.updateGasFilterDurability(serverPlayer);
        }
        
        // Apply damage every 2 seconds
        if (tickCounter % DAMAGE_INTERVAL == 0) {
            applyRadiationDamage(serverPlayer);
        }
        
        // Reset counter to prevent overflow
        if (tickCounter >= 1200) { // Reset every minute
            tickCounter = 0;
        }
    }
    
    private static void updateRadiationExposure(ServerPlayer player) {
        // Calculate current radiation level
        double totalRadiation = RadiationManager.getTotalRadiation(player.level(), player);
        
        // Check if player has complete radiation immunity from armor
        if (RadiationProtection.hasCompleteRadiationImmunity(player)) {
            // Player is immune - simulate being in a radiation-free environment for recovery
            // This allows natural recovery while preventing new exposure accumulation
            ExposureManager.updatePlayerExposure(player, 0.0); // 0.0 radiation triggers recovery
            
            // Don't apply new radiation effects, but preserve existing effects
            // so they can continue causing damage until they expire naturally
            return;
        }
        
        // Update player exposure normally
        ExposureManager.updatePlayerExposure(player, totalRadiation);
        
        // Apply radiation effects based on exposure
        applyRadiationEffects(player);
    }
    
    private static void applyRadiationEffects(ServerPlayer player) {
        double exposure = ExposureManager.getPlayerExposure(player);
        
        // Apply effects based on exposure thresholds (8 minutes = 9600 ticks)
        // Only apply if the player doesn't already have the appropriate effect
        if (exposure >= 5.0) {
            // Acute Radiation Syndrome (5+ mSv) - most severe
            if (!player.hasEffect(ModEffects.ACUTE_RADIATION_SYNDROME)) {
                // Remove lower-tier effects
                player.removeEffect(ModEffects.RADIATION_SICKNESS);
                player.removeEffect(ModEffects.RADIATION_POISONING);
                player.addEffect(new MobEffectInstance(ModEffects.ACUTE_RADIATION_SYNDROME, 9600, 0, false, true));
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§c☢ ACUTE RADIATION SYNDROME - CRITICAL!"));
            }
        } else if (exposure >= 3.0) {
            // Radiation Poisoning (3+ mSv) - moderate
            if (!player.hasEffect(ModEffects.RADIATION_POISONING)) {
                // Remove lower-tier effects and higher-tier effects
                player.removeEffect(ModEffects.RADIATION_SICKNESS);
                player.removeEffect(ModEffects.ACUTE_RADIATION_SYNDROME);
                player.addEffect(new MobEffectInstance(ModEffects.RADIATION_POISONING, 9600, 0, false, true));
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§6☢ RADIATION POISONING - SEVERE!"));
            }
        } else if (exposure >= 1.0) {
            // Radiation Sickness (1+ mSv) - mild
            if (!player.hasEffect(ModEffects.RADIATION_SICKNESS)) {
                // Remove higher-tier effects
                player.removeEffect(ModEffects.RADIATION_POISONING);
                player.removeEffect(ModEffects.ACUTE_RADIATION_SYNDROME);
                player.addEffect(new MobEffectInstance(ModEffects.RADIATION_SICKNESS, 9600, 0, false, true));
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§e☢ RADIATION SICKNESS - CAUTION!"));
            }
        } else {
            // No radiation effects if exposure is below 1.0
            player.removeEffect(ModEffects.RADIATION_SICKNESS);
            player.removeEffect(ModEffects.RADIATION_POISONING);
            player.removeEffect(ModEffects.ACUTE_RADIATION_SYNDROME);
        }
    }
    
    private static void applyRadiationDamage(ServerPlayer player) {
        // Apply damage based on active radiation effects, even if player has protection
        // This ensures players continue taking damage from existing effects until they expire
        if (player.hasEffect(ModEffects.ACUTE_RADIATION_SYNDROME)) {
            // Acute Radiation Syndrome: 4.5 damage every 2 seconds
            player.hurt(player.damageSources().magic(), 4.5f);
        } else if (player.hasEffect(ModEffects.RADIATION_POISONING)) {
            // Radiation Poisoning: 3.0 damage every 2 seconds
            player.hurt(player.damageSources().magic(), 3.0f);
        } else if (player.hasEffect(ModEffects.RADIATION_SICKNESS)) {
            // Radiation Sickness: 1.5 damage every 2 seconds
            player.hurt(player.damageSources().magic(), 1.5f);
        }
        
        // If player has no active radiation effects but has high exposure and no protection,
        // apply damage based on exposure level (fallback for edge cases)
        if (!player.hasEffect(ModEffects.RADIATION_SICKNESS) && 
            !player.hasEffect(ModEffects.RADIATION_POISONING) && 
            !player.hasEffect(ModEffects.ACUTE_RADIATION_SYNDROME) &&
            !RadiationProtection.hasCompleteRadiationImmunity(player)) {
            
            double exposure = ExposureManager.getPlayerExposure(player);
            
            if (exposure >= 5.0) {
                player.hurt(player.damageSources().magic(), 4.5f);
            } else if (exposure >= 3.0) {
                player.hurt(player.damageSources().magic(), 3.0f);
            } else if (exposure >= 1.0) {
                player.hurt(player.damageSources().magic(), 1.5f);
            }
        }
    }
}
