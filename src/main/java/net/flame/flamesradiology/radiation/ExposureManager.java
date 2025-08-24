package net.flame.flamesradiology.radiation;

import net.flame.flamesradiology.common.init.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages radiation exposure accumulation for players
 */
public class ExposureManager {
    private static final Map<UUID, Double> playerExposure = new HashMap<>();
    private static final Map<UUID, Long> lastExposureUpdate = new HashMap<>();
    
    // Exposure accumulation rate (exposure per second per mSv/t of radiation)
    private static final double EXPOSURE_RATE = 0.1; // 0.1 exposure per second per mSv/t
    private static final double MIN_RADIATION_FOR_EXPOSURE = 0.001; // Minimum radiation to start accumulating
    
    // Recovery rate when not exposed to radiation (much slower to simulate natural recovery)
    private static final double RECOVERY_RATE = 0.005; // 0.005 exposure recovered per second when safe
    
    /**
     * Update player exposure based on current radiation levels
     * @param player The player
     * @param currentRadiation Current total radiation level in mSv/t
     */
    public static void updatePlayerExposure(Player player, double currentRadiation) {
        UUID playerId = player.getUUID();
        long currentTime = System.currentTimeMillis();
        long lastUpdate = lastExposureUpdate.getOrDefault(playerId, currentTime);
        
        // Calculate time elapsed in seconds
        double deltaTime = (currentTime - lastUpdate) / 1000.0;
        
        if (deltaTime > 0) {
            double currentExposure = playerExposure.getOrDefault(playerId, 0.0);
            
            if (currentRadiation >= MIN_RADIATION_FOR_EXPOSURE) {
                // Accumulate exposure if radiation is above minimum threshold
                double exposureIncrease = currentRadiation * EXPOSURE_RATE * deltaTime;
                playerExposure.put(playerId, currentExposure + exposureIncrease);
            } else if (currentExposure > 0) {
                // Recover exposure when away from radiation
                double exposureDecrease = RECOVERY_RATE * deltaTime;
                double newExposure = Math.max(0.0, currentExposure - exposureDecrease);
                playerExposure.put(playerId, newExposure);
            }
        }
        
        lastExposureUpdate.put(playerId, currentTime);
        
        // Apply radiation effects based on exposure thresholds
        applyRadiationEffects(player);
    }
    
    /**
     * Apply radiation effects based on current exposure level
     * @param player The player to apply effects to
     */
    private static void applyRadiationEffects(Player player) {
        double exposure = getPlayerExposure(player);
        
        // Apply effects based on exposure thresholds (8 minutes = 9600 ticks)
        // Only apply if the player doesn't already have the appropriate effect
        if (exposure >= 5.0) {
            // Acute Radiation Syndrome (5+ mSv) - most severe
            if (!player.hasEffect(ModEffects.ACUTE_RADIATION_SYNDROME)) {
                // Remove lower-tier effects
                player.removeEffect(ModEffects.RADIATION_SICKNESS);
                player.removeEffect(ModEffects.RADIATION_POISONING);
                player.addEffect(new MobEffectInstance(ModEffects.ACUTE_RADIATION_SYNDROME, 9600, 0, false, true));
            }
        } else if (exposure >= 3.0) {
            // Radiation Poisoning (3+ mSv) - moderate
            if (!player.hasEffect(ModEffects.RADIATION_POISONING)) {
                // Remove lower-tier effects and higher-tier effects
                player.removeEffect(ModEffects.RADIATION_SICKNESS);
                player.removeEffect(ModEffects.ACUTE_RADIATION_SYNDROME);
                player.addEffect(new MobEffectInstance(ModEffects.RADIATION_POISONING, 9600, 0, false, true));
            }
        } else if (exposure >= 1.0) {
            // Radiation Sickness (1+ mSv) - mild
            if (!player.hasEffect(ModEffects.RADIATION_SICKNESS)) {
                // Remove higher-tier effects
                player.removeEffect(ModEffects.RADIATION_POISONING);
                player.removeEffect(ModEffects.ACUTE_RADIATION_SYNDROME);
                player.addEffect(new MobEffectInstance(ModEffects.RADIATION_SICKNESS, 9600, 0, false, true));
            }
        } else {
            // No radiation effects if exposure is below 1.0
            player.removeEffect(ModEffects.RADIATION_SICKNESS);
            player.removeEffect(ModEffects.RADIATION_POISONING);
            player.removeEffect(ModEffects.ACUTE_RADIATION_SYNDROME);
        }
    }
    
    /**
     * Get current exposure for a player
     * @param player The player
     * @return Current exposure level
     */
    public static double getPlayerExposure(Player player) {
        return playerExposure.getOrDefault(player.getUUID(), 0.0);
    }
    
    /**
     * Clear radiation exposure for a player
     * @param player The player to clear exposure for
     */
    public static void clearPlayerExposure(Player player) {
        playerExposure.remove(player.getUUID());
        lastExposureUpdate.remove(player.getUUID());
    }
    
    /**
     * Set player exposure (for testing or admin commands)
     * @param player The player
     * @param exposure Exposure level to set
     */
    public static void setPlayerExposure(Player player, double exposure) {
        playerExposure.put(player.getUUID(), Math.max(0.0, exposure));
    }
    
    /**
     * Reset player exposure to zero
     * @param player The player
     */
    public static void resetPlayerExposure(Player player) {
        playerExposure.put(player.getUUID(), 0.0);
    }
    
    /**
     * Get exposure danger level for display purposes
     * @param exposure Current exposure level
     * @return Danger level string
     */
    public static String getExposureDangerLevel(double exposure) {
        if (exposure >= 100.0) return "CRITICAL";
        if (exposure >= 50.0) return "SEVERE";
        if (exposure >= 25.0) return "HIGH";
        if (exposure >= 10.0) return "MODERATE";
        if (exposure >= 5.0) return "LOW";
        if (exposure >= 1.0) return "MINIMAL";
        return "SAFE";
    }
    
    /**
     * Get color for exposure level display
     * @param exposure Current exposure level
     * @return Color as integer (RGB)
     */
    public static int getExposureColor(double exposure) {
        if (exposure >= 100.0) return 0xFF0000;    // Red - CRITICAL
        if (exposure >= 50.0) return 0xFF4500;     // Orange Red - SEVERE
        if (exposure >= 25.0) return 0xFF8C00;     // Dark Orange - HIGH
        if (exposure >= 10.0) return 0xFFA500;     // Orange - MODERATE
        if (exposure >= 5.0) return 0xFFFF00;      // Yellow - LOW
        if (exposure >= 1.0) return 0x90EE90;      // Light Green - MINIMAL
        return 0xFFFFFF;                           // White - SAFE
    }
}
