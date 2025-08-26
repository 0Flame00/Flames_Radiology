package net.flame.flamesradiology.common.block;

import net.flame.flamesradiology.common.init.ModFluids;
import net.flame.flamesradiology.common.util.RadiationProtection;
import net.flame.flamesradiology.radiation.ExposureManager;
import net.flame.flamesradiology.radiation.RadiationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ContaminatedWaterBlock extends LiquidBlock {
    
    public ContaminatedWaterBlock() {
        super(ModFluids.CONTAMINATED_WATER_SOURCE.get(), BlockBehaviour.Properties.of()
                .mapColor(MapColor.WATER)
                .replaceable()
                .noCollission()
                .noOcclusion()
                .randomTicks()
                .strength(100.0F)
                .pushReaction(PushReaction.DESTROY)
                .noLootTable()
                .liquid());
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        
        if (entity instanceof Player player && !level.isClientSide) {
            // Only apply radiation exposure if player doesn't have hazmat suit protection
            if (!RadiationProtection.hasCompleteRadiationImmunity(player)) {
                double currentRadiation = RadiationManager.getTotalRadiation(level, player) + 5.0; // Add 5.0 mSv/t from contaminated water
                ExposureManager.updatePlayerExposure(player, currentRadiation);
            }
        }
        
        // Add some visual effects
        if (level.getRandom().nextInt(20) == 0) {
            level.addParticle(ParticleTypes.SMOKE,
                    entity.getX() + level.getRandom().nextGaussian() * 0.2D,
                    entity.getY() + level.getRandom().nextDouble() * entity.getBbHeight(),
                    entity.getZ() + level.getRandom().nextGaussian() * 0.2D,
                    0.0D, 0.1D, 0.0D);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        
        // Occasionally spawn particles to show contamination
        if (random.nextInt(10) == 0) {
            level.sendParticles(ParticleTypes.SMOKE,
                    pos.getX() + 0.5D,
                    pos.getY() + 1.0D,
                    pos.getZ() + 0.5D,
                    1, 0.0D, 0.0D, 0.0D, 0.01D);
        }
    }
}
