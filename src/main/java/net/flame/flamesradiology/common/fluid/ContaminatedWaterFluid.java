package net.flame.flamesradiology.common.fluid;

import net.flame.flamesradiology.common.init.ModBlocks;
import net.flame.flamesradiology.common.init.ModFluids;
import net.flame.flamesradiology.common.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

public abstract class ContaminatedWaterFluid extends FlowingFluid {
    
    @Override
    public @NotNull Fluid getFlowing() {
        return ModFluids.CONTAMINATED_WATER_FLOWING.get();
    }

    @Override
    public @NotNull Fluid getSource() {
        return ModFluids.CONTAMINATED_WATER_SOURCE.get();
    }

    @Override
    public @NotNull Item getBucket() {
        return ModItems.CONTAMINATED_WATER_BUCKET.get();
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(@NotNull FluidState fluidState) {
        return ModBlocks.CONTAMINATED_WATER_BLOCK.get().defaultBlockState()
                .setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
    }

    @Override
    public boolean isSame(@NotNull Fluid fluid) {
        return fluid == getSource() || fluid == getFlowing();
    }

    @Override
    protected int getDropOff(@NotNull LevelReader levelReader) {
        return 1;
    }

    @Override
    public int getTickDelay(@NotNull LevelReader levelReader) {
        return 5;
    }

    @Override
    protected boolean canConvertToSource(@NotNull Level level) {
        return false;
    }

    @Override
    protected int getSlopeFindDistance(@NotNull LevelReader levelReader) {
        return 4;
    }

    @Override
    public float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    public boolean canBeReplacedWith(@NotNull FluidState fluidState, @NotNull BlockGetter blockGetter, 
                                   @NotNull BlockPos blockPos, @NotNull Fluid fluid, @NotNull Direction direction) {
        // Match vanilla water behavior - only allow replacement by the same fluid or air
        return !fluidState.isSource() && direction == Direction.DOWN && !fluid.isSame(this);
    }

    @Override
    protected void beforeDestroyingBlock(@NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        // Empty implementation - contaminated water should behave like normal water and not destroy blocks
    }

    @Override
    protected boolean canSpreadTo(@NotNull BlockGetter blockGetter, @NotNull BlockPos fromPos, @NotNull BlockState fromBlockState, 
                                 @NotNull Direction direction, @NotNull BlockPos toPos, @NotNull BlockState toBlockState, 
                                 @NotNull FluidState toFluidState, @NotNull Fluid fluid) {
        // Use vanilla water behavior - delegate to parent implementation
        return super.canSpreadTo(blockGetter, fromPos, fromBlockState, direction, toPos, toBlockState, toFluidState, fluid);
    }

    @Override
    public @NotNull FluidType getFluidType() {
        return ModFluidTypes.CONTAMINATED_WATER_TYPE.get();
    }

    public static class Flowing extends ContaminatedWaterFluid {
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(@NotNull FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }

        @Override
        public boolean isSource(@NotNull FluidState fluidState) {
            return false;
        }
    }

    public static class Source extends ContaminatedWaterFluid {
        @Override
        public int getAmount(@NotNull FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isSource(@NotNull FluidState fluidState) {
            return true;
        }
    }
}
