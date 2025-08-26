package net.flame.flamesradiology.common.fluid;

import net.flame.flamesradiology.FlamesRadiology;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, FlamesRadiology.MOD_ID);

    public static final DeferredHolder<FluidType, FluidType> CONTAMINATED_WATER_TYPE = FLUID_TYPES.register("contaminated_water",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("fluid.flamesradiology.contaminated_water")
                    .canExtinguish(false)
                    .canDrown(true)
                    .canSwim(true)
                    .canHydrate(false)
                    .viscosity(1000)
                    .density(1000)
                    .lightLevel(0)
                    .temperature(300)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)) {
                @Override
                public void initializeClient(java.util.function.Consumer<net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions() {
                        private static final ResourceLocation STILL_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still");
                        private static final ResourceLocation FLOWING_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_flow");
                        private static final ResourceLocation OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_overlay");

                        @Override
                        public ResourceLocation getStillTexture() {
                            return STILL_TEXTURE;
                        }

                        @Override
                        public ResourceLocation getFlowingTexture() {
                            return FLOWING_TEXTURE;
                        }

                        @Override
                        public ResourceLocation getOverlayTexture() {
                            return OVERLAY_TEXTURE;
                        }

                        @Override
                        public int getTintColor() {
                            return 0xFF3F76E4; // Same blue tint as vanilla water
                        }
                    });
                }
            });

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
