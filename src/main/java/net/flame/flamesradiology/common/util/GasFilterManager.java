package net.flame.flamesradiology.common.util;

import net.flame.flamesradiology.common.init.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GasFilterManager {
    private static final String GAS_FILTER_TAG = "GasFilterDurability";
    private static final int MAX_GAS_FILTER_DURABILITY = 2400; // 2 minutes at 20 ticks per second
    
    /**
     * Gets the gas filter durability from a helmet
     */
    public static int getGasFilterDurability(ItemStack helmet) {
        if (helmet.isEmpty()) return 0;
        
        CompoundTag tag = helmet.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, 
                                            net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
        return tag.getInt(GAS_FILTER_TAG);
    }
    
    /**
     * Sets the gas filter durability for a helmet
     */
    public static void setGasFilterDurability(ItemStack helmet, int durability) {
        if (helmet.isEmpty()) return;
        
        CompoundTag tag = helmet.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, 
                                            net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
        tag.putInt(GAS_FILTER_TAG, Math.max(0, Math.min(durability, MAX_GAS_FILTER_DURABILITY)));
        helmet.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, 
                  net.minecraft.world.item.component.CustomData.of(tag));
    }
    
    /**
     * Decreases gas filter durability
     */
    public static void decreaseGasFilterDurability(ItemStack helmet, int amount) {
        int currentDurability = getGasFilterDurability(helmet);
        setGasFilterDurability(helmet, currentDurability - amount);
    }
    
    /**
     * Checks if helmet has active gas filter
     */
    public static boolean hasActiveGasFilter(ItemStack helmet) {
        return getGasFilterDurability(helmet) > 0;
    }
    
    /**
     * Replenishes gas filter using a gas filter item from inventory
     */
    public static boolean replenishGasFilter(Player player, ItemStack helmet) {
        if (helmet.isEmpty()) return false;
        
        // Find gas filter in inventory
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() == ModItems.GAS_FILTER.get()) {
                // Consume gas filter and replenish helmet
                stack.shrink(1);
                setGasFilterDurability(helmet, MAX_GAS_FILTER_DURABILITY);
                
                if (!player.level().isClientSide) {
                    player.sendSystemMessage(Component.literal("§aGas filter replenished!"));
                }
                return true;
            }
        }
        
        if (!player.level().isClientSide) {
            player.sendSystemMessage(Component.literal("§cNo gas filter found in inventory!"));
        }
        return false;
    }
    
    /**
     * Checks if helmet is a gas filter compatible helmet
     */
    public static boolean isGasFilterHelmet(ItemStack helmet) {
        return helmet.getItem() == ModItems.HAZMAT_HELMET.get() || 
               helmet.getItem() == ModItems.NETHERITE_HAZMAT_HELMET.get();
    }
    
    /**
     * Gets max gas filter durability
     */
    public static int getMaxGasFilterDurability() {
        return MAX_GAS_FILTER_DURABILITY;
    }
    
    /**
     * Updates gas filter durability for all players wearing helmets
     * Should be called every tick for players in radiation
     */
    public static void updateGasFilterDurability(Player player) {
        ItemStack helmet = player.getInventory().getArmor(3); // Helmet slot
        
        if (isGasFilterHelmet(helmet) && hasActiveGasFilter(helmet)) {
            // Only decrease durability if player is in a radioactive environment
            if (RadiationProtection.isInRadioactiveEnvironment(player)) {
                decreaseGasFilterDurability(helmet, 20); // Decrease by 20 ticks (1 second)
                
                // Notify player when filter is running low
                int durability = getGasFilterDurability(helmet);
                if (durability == 200 && !player.level().isClientSide) { // 10 seconds remaining
                    player.sendSystemMessage(Component.literal("§6⚠ Gas filter running low! (10s remaining)"));
                } else if (durability == 0 && !player.level().isClientSide) {
                    player.sendSystemMessage(Component.literal("§c☢ Gas filter depleted! Radiation protection disabled!"));
                }
            }
        }
    }
}
