package net.flame.flamesradiology.common.event;

import net.flame.flamesradiology.common.init.ModItems;
import net.flame.flamesradiology.network.RadiationSyncPacket;
import net.flame.flamesradiology.radiation.RadiationManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "flames_radiology")
public class RadiationSyncHandler {
    private static int tickCounter = 0;
    private static final int SYNC_INTERVAL = 20; // Sync every 20 ticks (1 second)
    
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
        if (serverPlayer.level().isClientSide) return;
        
        tickCounter++;
        if (tickCounter < SYNC_INTERVAL) return;
        tickCounter = 0;
        
        // Remove geiger counter requirement - radiation affects all players
        
        // Calculate current radiation level
        double environmentalRadiation = RadiationManager.calculateRadiationAt(serverPlayer.level(), serverPlayer.blockPosition());
        double inventoryRadiation = RadiationManager.calculateInventoryRadiation(serverPlayer);
        
        double environmentalRadiationInMSv = environmentalRadiation * 0.1;
        double inventoryRadiationInMSv = inventoryRadiation * 0.1;
        
        // Send packet to client
        PacketDistributor.sendToPlayer(serverPlayer, new RadiationSyncPacket(environmentalRadiationInMSv, inventoryRadiationInMSv));
    }
    
    private static boolean hasGeigerCounterInInventory(ServerPlayer player) {
        // Check all inventory slots including hotbar
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() == ModItems.GEIGER_COUNTER.get()) {
                return true;
            }
        }
        return false;
    }
}
