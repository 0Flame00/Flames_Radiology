package net.flame.flamesradiology.common.init;

import net.flame.flamesradiology.FlamesRadiology;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = 
        DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, FlamesRadiology.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER_CLICK_0 = registerSoundEvent("geiger_click_0");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER_CLICK_1 = registerSoundEvent("geiger_click_1");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER_CLICK_2 = registerSoundEvent("geiger_click_2");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER_CLICK_3 = registerSoundEvent("geiger_click_3");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER_CLICK_4 = registerSoundEvent("geiger_click_4");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIGER_CLICK_5 = registerSoundEvent("geiger_click_5");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(FlamesRadiology.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
