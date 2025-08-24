package net.flame.flamesradiology.network;

import net.flame.flamesradiology.client.overlay.RadiationOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RadiationSyncPacket(double environmentalRadiation, double inventoryRadiation) implements CustomPacketPayload {
    public static final Type<RadiationSyncPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("flames_radiology", "radiation_sync"));
    
    public static final StreamCodec<FriendlyByteBuf, RadiationSyncPacket> STREAM_CODEC = new StreamCodec<FriendlyByteBuf, RadiationSyncPacket>() {
        @Override
        public RadiationSyncPacket decode(FriendlyByteBuf buffer) {
            return new RadiationSyncPacket(buffer.readDouble(), buffer.readDouble());
        }

        @Override
        public void encode(FriendlyByteBuf buffer, RadiationSyncPacket packet) {
            buffer.writeDouble(packet.environmentalRadiation());
            buffer.writeDouble(packet.inventoryRadiation());
        }
    };
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    public static void handle(RadiationSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            // Update client-side radiation display
            RadiationOverlay.updateRadiation(packet.environmentalRadiation(), packet.inventoryRadiation());
        });
    }
}
