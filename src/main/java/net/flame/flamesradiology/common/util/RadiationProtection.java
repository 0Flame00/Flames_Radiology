package net.flame.flamesradiology.common.util;

import net.flame.flamesradiology.common.init.ModItems;
import net.flame.flamesradiology.radiation.RadiationManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RadiationProtection {
    
    /**
     * Checks if the player has complete radiation immunity from wearing full armor sets
     * Now includes gas filter durability check for helmets
     * @param player The player to check
     * @return true if player has complete radiation immunity, false otherwise
     */
    public static boolean hasCompleteRadiationImmunity(Player player) {
        return (hasFullHazmatSet(player) || hasFullNetheriteHazmatSet(player)) && hasActiveGasFilter(player);
    }
    
    /**
     * Checks if player has an active gas filter in their helmet
     * @param player The player to check
     * @return true if helmet has active gas filter, false otherwise
     */
    public static boolean hasActiveGasFilter(Player player) {
        ItemStack helmet = player.getInventory().getArmor(3); // Helmet slot
        return GasFilterManager.hasActiveGasFilter(helmet);
    }
    
    /**
     * Checks if the player is wearing a complete Hazmat suit
     * @param player The player to check
     * @return true if wearing full Hazmat set, false otherwise
     */
    public static boolean hasFullHazmatSet(Player player) {
        ItemStack helmet = player.getInventory().getArmor(3); // Helmet slot
        ItemStack chestplate = player.getInventory().getArmor(2); // Chestplate slot
        ItemStack leggings = player.getInventory().getArmor(1); // Leggings slot
        ItemStack boots = player.getInventory().getArmor(0); // Boots slot
        
        return helmet.getItem() == ModItems.HAZMAT_HELMET.get() &&
               chestplate.getItem() == ModItems.HAZMAT_CHESTPLATE.get() &&
               leggings.getItem() == ModItems.HAZMAT_LEGGINGS.get() &&
               boots.getItem() == ModItems.HAZMAT_BOOTS.get();
    }
    
    /**
     * Checks if the player is wearing a complete Netherite Hazmat suit
     * @param player The player to check
     * @return true if wearing full Netherite Hazmat set, false otherwise
     */
    public static boolean hasFullNetheriteHazmatSet(Player player) {
        ItemStack helmet = player.getInventory().getArmor(3); // Helmet slot
        ItemStack chestplate = player.getInventory().getArmor(2); // Chestplate slot
        ItemStack leggings = player.getInventory().getArmor(1); // Leggings slot
        ItemStack boots = player.getInventory().getArmor(0); // Boots slot
        
        return helmet.getItem() == ModItems.NETHERITE_HAZMAT_HELMET.get() &&
               chestplate.getItem() == ModItems.NETHERITE_HAZMAT_CHESTPLATE.get() &&
               leggings.getItem() == ModItems.NETHERITE_HAZMAT_LEGGINGS.get() &&
               boots.getItem() == ModItems.NETHERITE_HAZMAT_BOOTS.get();
    }
    
    /**
     * Gets the radiation protection level based on armor worn
     * @param player The player to check
     * @return Protection level: 1.0 = complete immunity, 0.0 = no protection
     */
    public static double getRadiationProtectionLevel(Player player) {
        if (hasCompleteRadiationImmunity(player)) {
            return 1.0; // Complete immunity
        }
        
        // Could add partial protection logic here in the future
        // For now, it's either full protection or none
        return 0.0;
    }
    
    /**
     * Checks if player is in a radioactive environment
     * @param player The player to check
     * @return true if player is exposed to radiation, false otherwise
     */
    public static boolean isInRadioactiveEnvironment(Player player) {
        double totalRadiation = RadiationManager.getTotalRadiation(player.level(), player);
        return totalRadiation > 0.001; // Minimum threshold for radiation exposure
    }
}
