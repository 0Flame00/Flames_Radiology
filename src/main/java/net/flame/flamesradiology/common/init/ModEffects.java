package net.flame.flamesradiology.common.init;

import net.flame.flamesradiology.FlamesRadiology;
import net.flame.flamesradiology.common.effect.AcuteRadiationSyndromeEffect;
import net.flame.flamesradiology.common.effect.RadiationPoisoningEffect;
import net.flame.flamesradiology.common.effect.RadiationSicknessEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, FlamesRadiology.MOD_ID);
    
    public static final DeferredHolder<MobEffect, RadiationSicknessEffect> RADIATION_SICKNESS = 
        EFFECTS.register("radiation_sickness", RadiationSicknessEffect::new);
    
    public static final DeferredHolder<MobEffect, RadiationPoisoningEffect> RADIATION_POISONING = 
        EFFECTS.register("radiation_poisoning", RadiationPoisoningEffect::new);
    
    public static final DeferredHolder<MobEffect, AcuteRadiationSyndromeEffect> ACUTE_RADIATION_SYNDROME = 
        EFFECTS.register("acute_radiation_syndrome", AcuteRadiationSyndromeEffect::new);
    
    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
