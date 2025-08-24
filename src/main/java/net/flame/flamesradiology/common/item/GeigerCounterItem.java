package net.flame.flamesradiology.common.item;

import net.flame.flamesradiology.common.init.ModSounds;
import net.flame.flamesradiology.radiation.RadiationManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class GeigerCounterItem extends Item {
    private static final Map<UUID, Long> lastClickTime = new HashMap<>();
    private static final Map<UUID, Integer> burstCounter = new HashMap<>();
    private static final Random random = new Random();

    public GeigerCounterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        // Toggle on/off when right-clicking (no shift required)
        boolean isEnabled = isGeigerEnabled(itemStack);
        setGeigerEnabled(itemStack, !isEnabled);
        
        if (!level.isClientSide) {
            String status = !isEnabled ? "ON" : "OFF";
            player.sendSystemMessage(Component.literal("Geiger Counter: " + status)
                .withStyle(!isEnabled ? ChatFormatting.GREEN : ChatFormatting.RED));
        }
        
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    public void performAutomaticScan(Level level, Player player, ItemStack geigerCounter) {
        if (!level.isClientSide || !isGeigerEnabled(geigerCounter)) {
            return; // Only run on client side for sound playing and when enabled
        }
        
        double totalRadiation = RadiationManager.getTotalRadiation(level, player);
        // Always ensure some background radiation for clicking
        if (totalRadiation < 0.001) {
            totalRadiation = 0.001; // Minimum background radiation
        }
        playGeigerClickAutomatic(level, player, totalRadiation);
    }
    
    /**
     * Check if geiger counter is enabled (defaults to true for new items)
     */
    public static boolean isGeigerEnabled(ItemStack stack) {
        var customData = stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
        var tag = customData.copyTag();
        // Default to enabled if no tag exists
        return !tag.contains("enabled") || tag.getBoolean("enabled");
    }
    
    /**
     * Set geiger counter enabled state
     */
    public static void setGeigerEnabled(ItemStack stack, boolean enabled) {
        stack.update(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY, 
                customData -> customData.update(tag -> tag.putBoolean("enabled", enabled)));
    }
    
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, java.util.List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        boolean isEnabled = isGeigerEnabled(stack);
        String status = isEnabled ? "ON" : "OFF";
        ChatFormatting color = isEnabled ? ChatFormatting.GREEN : ChatFormatting.RED;
        
        tooltipComponents.add(Component.literal("Status: " + status).withStyle(color));
        tooltipComponents.add(Component.literal("Right-click to toggle").withStyle(ChatFormatting.GRAY));
        
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private void playGeigerSound(Level level, Player player, int detectionCount) {
        if (!level.isClientSide) {
            double totalRadiation = RadiationManager.getTotalRadiation(level, player);
            playGeigerClick(level, player, totalRadiation);
        }
    }

    private static long getClickInterval(double radiation) {
        // Background radiation always provides occasional clicks
        if (radiation <= 0.001) return 8000; // Background radiation (1 click every 8 seconds)
        
        // Scale clicking frequency with radiation level
        if (radiation > 5.000) return 50;   // Very rapid clicking (20 clicks/second)
        if (radiation > 2.000) return 100;  // Rapid clicking (10 clicks/second)
        if (radiation > 1.000) return 200;  // Fast clicking (5 clicks/second)
        if (radiation > 0.500) return 500;  // Medium clicking (2 clicks/second)
        if (radiation > 0.100) return 1000; // Slow clicking (1 click/second)
        if (radiation > 0.050) return 2000; // Very slow clicking (0.5 clicks/second)
        if (radiation > 0.010) return 3000; // Occasional clicking
        if (radiation > 0.001) return 5000; // Very occasional clicking for very low radiation
        
        return 8000; // Fallback for background radiation
    }

    private static String getRadiationDangerLevel(double radiation) {
        if (radiation >= 0.2000) return "LETHAL";        // GEIGER_CLICK_5 at 0.3500+ mSv/t
        if (radiation >= 0.1500) return "EXTREME";       // GEIGER_CLICK_4 at 0.2500+ mSv/t
        if (radiation >= 0.1000) return "VERY HIGH";     // GEIGER_CLICK_3 at 0.1500+ mSv/t
        if (radiation >= 0.0500) return "HIGH";          // GEIGER_CLICK_2 at 0.1000+ mSv/t
        if (radiation >= 0.0100) return "MODERATE";      // GEIGER_CLICK_1 at 0.0100+ mSv/t
        if (radiation >= 0.0001) return "LOW";           // GEIGER_CLICK_0 at 0.0001+ mSv/t
        return "MINIMAL";                               // Background radiation
    }

    private static void playGeigerClick(Level level, Player player, double totalRadiation) {
        SoundEvent sound = switch (getRadiationDangerLevel(totalRadiation)) {
            case "LETHAL" -> ModSounds.GEIGER_CLICK_5.get();
            case "EXTREME" -> ModSounds.GEIGER_CLICK_4.get();
            case "VERY HIGH" -> ModSounds.GEIGER_CLICK_3.get();
            case "HIGH" -> ModSounds.GEIGER_CLICK_2.get();
            case "MODERATE" -> ModSounds.GEIGER_CLICK_1.get();
            case "LOW" -> ModSounds.GEIGER_CLICK_0.get();
            default -> ModSounds.GEIGER_CLICK_0.get(); // MINIMAL and any other cases
        };

        // Volume increases with radiation level
        float volume = Math.min(1.0f, 0.3f + (float)(totalRadiation * 0.1f));
        
        // Pitch increases significantly with radiation level for higher clicking sound
        float pitch = 1.0f + (float)(totalRadiation * 0.3f); // More dramatic pitch increase
        pitch = Math.min(2.0f, Math.max(0.8f, pitch)); // Clamp between 0.8 and 2.0

        level.playSound(player, player.blockPosition(), sound, SoundSource.PLAYERS, volume, pitch);
    }

    private void playGeigerClickAutomatic(Level level, Player player, double totalRadiation) {
        UUID playerId = player.getUUID();
        long currentTime = System.currentTimeMillis();
        long lastClick = lastClickTime.getOrDefault(playerId, 0L);
        int burstCount = burstCounter.getOrDefault(playerId, 0);
        
        // Calculate base interval with randomization for realistic timing
        long baseInterval = getClickInterval(totalRadiation);
        
        // Add random variation (±30% of base interval) for realistic Geiger counter behavior
        double randomFactor = 0.7 + (random.nextDouble() * 0.6); // 0.7 to 1.3 multiplier
        long randomizedInterval = (long)(baseInterval * randomFactor);
        
        // Handle burst patterns - real Geiger counters often have rapid bursts
        boolean shouldBurst = false;
        long burstInterval = 50; // Very fast clicking during bursts
        
        if (burstCount > 0) {
            // Continue existing burst
            if (currentTime - lastClick >= burstInterval) {
                playGeigerClick(level, player, totalRadiation);
                lastClickTime.put(playerId, currentTime);
                burstCounter.put(playerId, burstCount - 1);
            }
            return;
        }
        
        // Check if we should start a new burst (higher radiation = more likely)
        if (totalRadiation > 0.1) {
            double burstChance = Math.min(0.15, totalRadiation * 0.1); // Max 15% chance
            shouldBurst = random.nextDouble() < burstChance;
        }
        
        if (currentTime - lastClick >= randomizedInterval) {
            playGeigerClick(level, player, totalRadiation);
            lastClickTime.put(playerId, currentTime);
            
            // Start a burst if triggered
            if (shouldBurst) {
                int burstSize = 2 + random.nextInt(4); // 2-5 rapid clicks
                burstCounter.put(playerId, burstSize);
            }
        }
    }
}
