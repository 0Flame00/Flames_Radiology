package net.flame.flamesradiology.common.item;

import net.flame.flamesradiology.common.init.ModEffects;
import net.flame.flamesradiology.radiation.ExposureManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Potassium Iodide item that cures radiation effects when consumed
 */
public class PotassiumIodideItem extends Item {
    
    public PotassiumIodideItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player && !level.isClientSide) {
            // Remove all radiation effects
            player.removeEffect(ModEffects.RADIATION_SICKNESS);
            player.removeEffect(ModEffects.RADIATION_POISONING);
            player.removeEffect(ModEffects.ACUTE_RADIATION_SYNDROME);
            
            // Clear radiation exposure
            ExposureManager.clearPlayerExposure(player);
        }
        
        return super.finishUsingItem(stack, level, entity);
    }
}
