package net.flame.flamesradiology.common.init;

import net.flame.flamesradiology.FlamesRadiology;
import net.flame.flamesradiology.common.fluid.ContaminatedWaterFluid;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(BuiltInRegistries.FLUID, FlamesRadiology.MOD_ID);

    public static final DeferredHolder<Fluid, FlowingFluid> CONTAMINATED_WATER_SOURCE = FLUIDS.register("contaminated_water",
            ContaminatedWaterFluid.Source::new);
    
    public static final DeferredHolder<Fluid, FlowingFluid> CONTAMINATED_WATER_FLOWING = FLUIDS.register("contaminated_water_flowing",
            ContaminatedWaterFluid.Flowing::new);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
