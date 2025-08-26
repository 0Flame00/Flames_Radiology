package net.flame.flamesradiology.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class AcuteRadiationSyndromeEffect extends MobEffect {
    
    public AcuteRadiationSyndromeEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF0000); // Red color
    }
    
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // Apply effect every 30 seconds (600 ticks) for vanilla effects only
        return duration % 600 == 0;
    }
    
    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player) {
            // Severe hunger effect - drain 5 food levels (strongest)
            if (player.getFoodData().getFoodLevel() > 5) {
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 5);
            } else if (player.getFoodData().getFoodLevel() > 0) {
                player.getFoodData().setFoodLevel(0); // Drain remaining food
            }

            // Apply hunger effect to make food less effective (1 minute duration)
            if (!player.hasEffect(MobEffects.HUNGER)) {
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 1200, 3, false, false)); // Level 3 for strongest effect
            }
            
            // Apply weakness (level 1 - stronger than other effects, 1 minute duration) - only if not already present
            if (!player.hasEffect(MobEffects.WEAKNESS)) {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1200, 1, false, false));
            }
            
            // Apply slowness (level 1 - stronger than other effects, 1 minute duration) - only if not already present
            if (!player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 1, false, false));
            }
            
            // Apply wither effect (continuous damage over time, 1 minute duration) - only if not already present
            if (!player.hasEffect(MobEffects.WITHER)) {
                player.addEffect(new MobEffectInstance(MobEffects.WITHER, 1200, 0, false, true));
            }
            
            // Apply blindness (constant, 1 minute duration) - only if not already present
            if (!player.hasEffect(MobEffects.BLINDNESS)) {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 1200, 0, false, true));
            }
            
            // Apply nausea (constant, 1 minute duration) - only if not already present
            if (!player.hasEffect(MobEffects.CONFUSION)) {
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 1200, 0, false, true));
            }
            
            // Occasional magic damage (90% chance, strongest)
            if (player.getRandom().nextFloat() < 0.30f) {
                // Try multiple damage approaches to ensure it works
                player.hurt(player.damageSources().magic(), 3.0f);
                // Also try generic damage as backup
                player.hurt(player.damageSources().generic(), 1.5f);
            }
        }
        return true;
    }
}
