package net.flame.flamesradiology.radiation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Represents a radioactive source with position and intensity
 */
public class RadiationSource {
    private final BlockPos position;
    private final double intensity;
    private final RadiationSourceType type;
    
    public RadiationSource(BlockPos position, double intensity, RadiationSourceType type) {
        this.position = position;
        this.intensity = intensity;
        this.type = type;
    }
    
    /**
     * Calculate radiation level at a given position using inverse square law
     * Formula: Radiation = Intensity / (distance^2 + 1)
     * The +1 prevents division by zero and provides base radiation at source
     * 
     * @param targetPos Position to calculate radiation at
     * @return Radiation level at target position
     */
    public double getRadiationAt(BlockPos targetPos) {
        double distance = Math.sqrt(position.distSqr(targetPos));
        
        // Inverse square law with minimum distance of 1 to prevent infinite radiation
        double effectiveDistance = Math.max(distance, 1.0);
        double radiationLevel = intensity / (effectiveDistance * effectiveDistance);
        
        // Apply type-specific modifiers
        return radiationLevel * type.getModifier();
    }
    
    /**
     * Check if this radiation source affects the given position within max range
     * @param targetPos Position to check
     * @param maxRange Maximum effective range
     * @return true if position is within range and receives meaningful radiation
     */
    public boolean affectsPosition(BlockPos targetPos, double maxRange) {
        double distance = Math.sqrt(position.distSqr(targetPos));
        if (distance > maxRange) {
            return false;
        }
        
        // Check if radiation level is above minimum threshold (0.1)
        return getRadiationAt(targetPos) >= 0.1;
    }
    
    /**
     * Get the maximum effective range of this radiation source
     * Range where radiation drops to 0.1 level
     * @return Maximum range in blocks
     */
    public double getMaxRange() {
        // Calculate distance where radiation = 0.1
        // 0.1 = intensity / (distance^2)
        // distance^2 = intensity / 0.1
        // distance = sqrt(intensity * 10)
        return Math.sqrt(intensity * type.getModifier() * 10.0);
    }
    
    public BlockPos getPosition() {
        return position;
    }
    
    public double getIntensity() {
        return intensity;
    }
    
    public RadiationSourceType getType() {
        return type;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RadiationSource that = (RadiationSource) obj;
        return position.equals(that.position) && 
               Double.compare(that.intensity, intensity) == 0 && 
               type == that.type;
    }
    
    @Override
    public int hashCode() {
        return position.hashCode() * 31 + Double.hashCode(intensity) + type.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("RadiationSource{pos=%s, intensity=%.2f, type=%s, range=%.1f}", 
            position, intensity, type, getMaxRange());
    }
}
