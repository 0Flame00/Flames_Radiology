package net.flame.flamesradiology.common.event;

import net.flame.flamesradiology.common.command.RadiationCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber
public class CommandEventHandler {
    
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        RadiationCommand.register(event.getDispatcher());
    }
}
