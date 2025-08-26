package net.flame.flamesradiology.client.overlay;

import net.flame.flamesradiology.common.init.ModItems;
import net.flame.flamesradiology.common.item.GeigerCounterItem;
import net.flame.flamesradiology.common.util.GasFilterManager;
import net.flame.flamesradiology.common.util.RadiationProtection;
import net.flame.flamesradiology.radiation.ExposureManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nonnull;
import java.util.Random;

public class RadiationOverlay implements LayeredDraw.Layer {
    private static double currentEnvironmentalRadiation = 0.0;
    private static double currentInventoryRadiation = 0.0;
    private static final double BACKGROUND_RADIATION_MIN = 0.0001;
    private static final double BACKGROUND_RADIATION_MAX = 0.0005;
    private static final Random random = new Random();
    
    // Background radiation fluctuation control
    private static double cachedBackgroundRadiation = BACKGROUND_RADIATION_MIN;
    private static long lastBackgroundUpdate = 0;
    private static final long BACKGROUND_UPDATE_INTERVAL = 5000; // 5 seconds in milliseconds
    
    public static void updateRadiation(double environmentalRadiation, double inventoryRadiation) {
        currentEnvironmentalRadiation = environmentalRadiation;
        currentInventoryRadiation = inventoryRadiation;
    }
    
    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, @Nonnull DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        
        if (player == null) return;
        
        // Check if player is holding Geiger Counter in main hand or offhand
        if (!isHoldingGeigerCounter(player)) return;
        
        // Calculate background radiation with slow fluctuation
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackgroundUpdate > BACKGROUND_UPDATE_INTERVAL) {
            cachedBackgroundRadiation = BACKGROUND_RADIATION_MIN + 
                (BACKGROUND_RADIATION_MAX - BACKGROUND_RADIATION_MIN) * random.nextDouble();
            lastBackgroundUpdate = currentTime;
        }
        double backgroundRadiation = cachedBackgroundRadiation;
        
        // Total environmental radiation = background + detected environmental radiation
        // Ensure background radiation never exceeds the maximum limit
        double clampedBackgroundRadiation = Math.min(backgroundRadiation, BACKGROUND_RADIATION_MAX);
        double totalEnvironmentalRadiation = clampedBackgroundRadiation + currentEnvironmentalRadiation;
        double totalRadiation = totalEnvironmentalRadiation + currentInventoryRadiation;
        
        // Check radiation protection status
        boolean hasImmunity = RadiationProtection.hasCompleteRadiationImmunity(player);
        
        // Only update exposure if not immune (UI should not interfere with server-side logic)
        if (!hasImmunity) {
            ExposureManager.updatePlayerExposure(player, totalRadiation);
        }
        double currentExposure = ExposureManager.getPlayerExposure(player);
        String protectionStatus = hasImmunity ? "PROTECTED" : "EXPOSED";
        
        // Radiation symbol and header
        String headerText = "☢ GEIGER COUNTER";
        String envRadiationText = String.format("Environmental: %.4f mSv/t", totalEnvironmentalRadiation);
        String invRadiationText = String.format("Inventory: %.4f mSv/t", currentInventoryRadiation);
        String exposureText = String.format("Exposure: %.2f (%s)", currentExposure, ExposureManager.getExposureDangerLevel(currentExposure));
        String protectionText = String.format("Status: %s", protectionStatus);
        
        // Position: 10 pixels from left, 10 pixels from top
        int x = 10;
        int y = 10;
        int lineHeight = minecraft.font.lineHeight + 2;
        
        // Draw header with radiation symbol in bright yellow (smaller scale)
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.8f, 0.8f, 1.0f);
        
        // Adjust positions for smaller scale
        int scaledX = (int)(x / 0.8f);
        int scaledY = (int)(y / 0.8f);
        int scaledLineHeight = (int)(lineHeight / 0.8f);
        
        guiGraphics.drawString(minecraft.font, headerText, scaledX, scaledY, 0xFFFF00);
        
        // Draw environmental radiation text - color based on level
        int envColor = getRadiationColor(totalEnvironmentalRadiation);
        guiGraphics.drawString(minecraft.font, envRadiationText, scaledX, scaledY + scaledLineHeight, envColor);
        
        // Draw inventory radiation text in orange
        guiGraphics.drawString(minecraft.font, invRadiationText, scaledX, scaledY + scaledLineHeight * 2, 0xFFA500);
        
        // Draw exposure with color based on danger level
        int exposureColor = ExposureManager.getExposureColor(currentExposure);
        guiGraphics.drawString(minecraft.font, exposureText, scaledX, scaledY + scaledLineHeight * 3, exposureColor);
        
        // Draw protection status
        int protectionColor = hasImmunity ? 0x00FF00 : 0xFF4444; // Green if protected, red if exposed
        guiGraphics.drawString(minecraft.font, protectionText, scaledX, scaledY + scaledLineHeight * 4, protectionColor);
        
        guiGraphics.pose().popPose();
        
        // Draw gas filter durability UI in bottom left corner
        renderGasFilterUI(guiGraphics, player, minecraft);
    }
    
    private boolean isHoldingGeigerCounter(Player player) {
        // Check if player has an ENABLED Geiger Counter anywhere in their inventory
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem() == ModItems.GEIGER_COUNTER.get() && 
                GeigerCounterItem.isGeigerEnabled(itemStack)) {
                return true;
            }
        }
        
        // Also check offhand slot
        ItemStack offHandItem = player.getOffhandItem();
        if (offHandItem.getItem() == ModItems.GEIGER_COUNTER.get() && 
            GeigerCounterItem.isGeigerEnabled(offHandItem)) {
            return true;
        }
        
        return false;
    }
    
    private void renderGasFilterUI(GuiGraphics guiGraphics, Player player, Minecraft minecraft) {
        ItemStack helmet = player.getInventory().getArmor(3); // Helmet slot
        
        // Only show gas filter UI if wearing a compatible helmet
        if (!GasFilterManager.isGasFilterHelmet(helmet)) return;
        
        int durability = GasFilterManager.getGasFilterDurability(helmet);
        int maxDurability = GasFilterManager.getMaxGasFilterDurability();
        
        // Position: bottom left corner, above hotbar
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        int x = 10;
        int y = screenHeight - 60; // Above hotbar
        
        // Draw gas filter icon and durability bar
        String filterText = "⚡ Gas Filter";
        int textColor = durability > 0 ? 0x00FF00 : 0xFF0000; // Green if active, red if depleted
        
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.8f, 0.8f, 1.0f);
        
        int scaledX = (int)(x / 0.8f);
        int scaledY = (int)(y / 0.8f);
        
        // Draw text
        guiGraphics.drawString(minecraft.font, filterText, scaledX, scaledY, textColor);
        
        // Draw durability bar
        int barWidth = 60;
        int barHeight = 4;
        int barY = scaledY + minecraft.font.lineHeight + 2;
        
        // Background bar (dark gray)
        guiGraphics.fill(scaledX, barY, scaledX + barWidth, barY + barHeight, 0xFF333333);
        
        // Durability bar (color based on remaining durability)
        if (durability > 0) {
            int fillWidth = (int)((double)durability / maxDurability * barWidth);
            int barColor;
            
            if (durability > maxDurability * 0.5) {
                barColor = 0xFF00FF00; // Green
            } else if (durability > maxDurability * 0.2) {
                barColor = 0xFFFFFF00; // Yellow
            } else {
                barColor = 0xFFFF0000; // Red
            }
            
            guiGraphics.fill(scaledX, barY, scaledX + fillWidth, barY + barHeight, barColor);
        }
        
        // Draw time remaining
        if (durability > 0) {
            int seconds = durability / 20;
            String timeText = String.format("%ds", seconds);
            guiGraphics.drawString(minecraft.font, timeText, scaledX + barWidth + 5, scaledY, 0xFFFFFF);
        } else {
            guiGraphics.drawString(minecraft.font, "DEPLETED", scaledX + barWidth + 5, scaledY, 0xFF0000);
        }
        
        guiGraphics.pose().popPose();
    }
    
    private int getRadiationColor(double radiation) {
        // Color coding based on radiation danger levels (matching Geiger counter thresholds)
        if (radiation >= 0.2000) return 0xFF0000;    // Red - LETHAL
        if (radiation >= 0.1500) return 0xFF4500;    // Orange Red - EXTREME  
        if (radiation >= 0.1000) return 0xFF8C00;    // Dark Orange - VERY HIGH
        if (radiation >= 0.0500) return 0xFFA500;    // Orange - HIGH
        if (radiation >= 0.0100) return 0xFFFF00;    // Yellow - MODERATE
        if (radiation >= 0.0001) return 0x90EE90;    // Light Green - LOW
        return 0xFFFFFF;                             // White - MINIMAL
    }
}
