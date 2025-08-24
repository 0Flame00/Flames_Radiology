package net.flame.flamesradiology.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class RadiationSicknessEffect extends MobEffect {
    
    public RadiationSicknessEffect() {
        super(MobEffectCategory.HARMFUL, 0x90EE90); // Light green color
    }
    
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // Apply effect every 30 seconds (600 ticks) for vanilla effects only
        return duration % 600 == 0;
    }
    
    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player) {
            // Apply stronger hunger effect - drain 3 food levels
            if (player.getFoodData().getFoodLevel() > 3) {
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 3);
            } else if (player.getFoodData().getFoodLevel() > 0) {
                player.getFoodData().setFoodLevel(0); // Drain remaining food
            }
            
            // Apply slowness (1 minute duration) - only if not already present
            if (!player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1200, 0, false, false));
            }

            // Apply hunger effect to make food less effective (1 minute duration)
            if (!player.hasEffect(MobEffects.HUNGER)) {
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 1200, 1, false, false)); // Level 1 for stronger effect
            }
            
            // Occasional nausea (20% chance, 1 minute duration) - only if not already present
            if (player.getRandom().nextFloat() < 0.2f && !player.hasEffect(MobEffects.CONFUSION)) {
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 1200, 0, false, true));
            }

            // Occasional damage (30% chance to match other effects)
            if (player.getRandom().nextFloat() < 0.30f) {
                // Try multiple damage approaches to ensure it works
                player.hurt(player.damageSources().magic(), 1.0f);
                // Also try generic damage as backup
                player.hurt(player.damageSources().generic(), 0.5f);
            }
            
        }
        return true;
    }
}
