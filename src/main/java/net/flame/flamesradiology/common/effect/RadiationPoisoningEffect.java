package net.flame.flamesradiology.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class RadiationPoisoningEffect extends MobEffect {
    
    public RadiationPoisoningEffect() {
        super(MobEffectCategory.HARMFUL, 0xFFFF00); // Yellow color
    }
    
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // Apply effect every 30 seconds (600 ticks) for vanilla effects only
        return duration % 600 == 0;
    }
    
    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player) {
            // Apply stronger hunger effect - drain 4 food levels
            if (player.getFoodData().getFoodLevel() > 4) {
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 4);
            } else if (player.getFoodData().getFoodLevel() > 0) {
                player.getFoodData().setFoodLevel(0); // Drain remaining food
            }

            // Apply hunger effect to make food less effective (1 minute duration)
            if (!player.hasEffect(MobEffects.HUNGER)) {
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 1200, 2, false, false)); // Level 2 for stronger effect
            }
            
            // Apply weakness effect (1 minute duration) - only if not already present
            if (!player.hasEffect(MobEffects.WEAKNESS)) {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1200, 0, false, false));
            }
            
            // Apply slowness (1 minute duration) - only if not already present
            if (!player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 0, false, false));
            }
            
            // Apply nausea (more frequent than radiation sickness, 1 minute duration) - only if not already present
            if (!player.hasEffect(MobEffects.CONFUSION)) {
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 1200, 0, false, true));
            }
            
            // Occasional blindness (25% chance, 1 minute duration) - only if not already present
            if (player.getRandom().nextFloat() < 0.25f && !player.hasEffect(MobEffects.BLINDNESS)) {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 1200, 0, false, true));
            }
            
            // Occasional magic damage (85% chance, stronger than radiation sickness)
            if (player.getRandom().nextFloat() < 0.25f) {
                // Try multiple damage approaches to ensure it works
                player.hurt(player.damageSources().magic(), 2.0f);
                // Also try generic damage as backup
                player.hurt(player.damageSources().generic(), 1.0f);
            }
        }
        return true;
    }
}
