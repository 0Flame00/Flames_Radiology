package net.flame.flamesradiology.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("flames_radiology");
        
        registrar.playToClient(
            RadiationSyncPacket.TYPE,
            RadiationSyncPacket.STREAM_CODEC,
            RadiationSyncPacket::handle
        );
    }
}
