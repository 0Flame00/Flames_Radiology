package net.flame.flamesradiology.common.event;

import net.flame.flamesradiology.common.init.ModEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class RadiationDamageHandler {
    
    @SubscribeEvent
    public static void onLivingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        
        float damageMultiplier = 1.0f;
        
        // Check for radiation effects and apply damage multipliers
        if (player.hasEffect(ModEffects.ACUTE_RADIATION_SYNDROME)) {
            // Acute Radiation Syndrome: +50% damage taken (most severe)
            damageMultiplier = 1.5f;
        } else if (player.hasEffect(ModEffects.RADIATION_POISONING)) {
            // Radiation Poisoning: +25% damage taken (moderate)
            damageMultiplier = 1.25f;
        } else if (player.hasEffect(ModEffects.RADIATION_SICKNESS)) {
            // Radiation Sickness: +10% damage taken (mild)
            damageMultiplier = 1.1f;
        }
        
        // Apply the damage multiplier if any radiation effect is present
        if (damageMultiplier > 1.0f) {
            float originalDamage = event.getAmount();
            float newDamage = originalDamage * damageMultiplier;
            event.setAmount(newDamage);
        }
    }
}
