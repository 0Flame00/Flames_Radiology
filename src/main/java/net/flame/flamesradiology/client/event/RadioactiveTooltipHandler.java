package net.flame.flamesradiology.client.event;

import net.flame.flamesradiology.registry.RadioactiveSourceRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(Dist.CLIENT)
public class RadioactiveTooltipHandler {
    
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        
        // Get radiation level for this item
        double radiationLevel = RadioactiveSourceRegistry.getRadiationLevel(stack);
        
        // Only add tooltip if item is radioactive
        if (radiationLevel > 0) {
            // Format radiation level to 3 decimal places
            String radiationText = String.format("§6☢ Radioactivity: %.3f mSv/t", radiationLevel);
            event.getToolTip().add(Component.literal(radiationText));
        }
    }
}
