package net.flame.flamesradiology.common.init;

import net.flame.flamesradiology.FlamesRadiology;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FlamesRadiology.MOD_ID);

    public static final Supplier<CreativeModeTab> FLAMES_RADIOLOGY_TAB = CREATIVE_MODE_TABS.register("flames_radiology_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.flames_radiology_tab"))
                    .icon(() -> new ItemStack(ModItems.GEIGER_COUNTER.get()))
                    .displayItems((parameters, output) -> {
                        // Add items to the creative tab
                        output.accept(ModItems.GEIGER_COUNTER.get());
                        output.accept(ModItems.LEAD_INGOT.get());
                        output.accept(ModItems.LEAD_NUGGET.get());
                        output.accept(ModItems.RAW_LEAD.get());
                        output.accept(ModItems.BORON_INGOT.get());
                        output.accept(ModItems.BORON_NUGGET.get());
                        output.accept(ModItems.RAW_BORON.get());
                        output.accept(ModItems.POTASSIUM.get());
                        output.accept(ModItems.RUBBER.get());
                        output.accept(ModItems.ACTIVATED_CARBON.get());
                        output.accept(ModItems.GLASS_TUBE.get());
                        output.accept(ModItems.FABRIC.get());
                        output.accept(ModItems.GAS_FILTER.get());
                        output.accept(ModItems.IODINE.get());
                        output.accept(ModItems.POTASSIUM_IODIDE.get());
                        
                        // Hazmat Suit Armor
                        output.accept(ModItems.HAZMAT_HELMET.get());
                        output.accept(ModItems.HAZMAT_CHESTPLATE.get());
                        output.accept(ModItems.HAZMAT_LEGGINGS.get());
                        output.accept(ModItems.HAZMAT_BOOTS.get());
                        
                        // Netherite Hazmat Suit Armor
                        output.accept(ModItems.NETHERITE_HAZMAT_HELMET.get());
                        output.accept(ModItems.NETHERITE_HAZMAT_CHESTPLATE.get());
                        output.accept(ModItems.NETHERITE_HAZMAT_LEGGINGS.get());
                        output.accept(ModItems.NETHERITE_HAZMAT_BOOTS.get());
<<<<<<< HEAD
                        
                        // Fluid Items
                        output.accept(ModItems.CONTAMINATED_WATER_BUCKET.get());
                        
=======
>>>>>>> 3598fac37cb55863843246fb1d6a25e626ceaf45
                        // Add blocks if you have any
                        output.accept(ModBlocks.BORON_BLOCK.get());
                        output.accept(ModBlocks.LEAD_BLOCK.get());
                        output.accept(ModBlocks.RAW_BORON_BLOCK.get());
                        output.accept(ModBlocks.RAW_LEAD_BLOCK.get());
                        output.accept(ModBlocks.RAW_POLY_HALITE_BLOCK.get());
                        output.accept(ModBlocks.BORON_ORE.get());
                        output.accept(ModBlocks.LEAD_ORE.get());
                        output.accept(ModBlocks.POLY_HALITE_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_BORON_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_LEAD_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_POLY_HALITE_ORE.get());
<<<<<<< HEAD
                        
                        // Contaminated Blocks
                        output.accept(ModBlocks.WASTE_LAND_DIRT.get());
                        output.accept(ModBlocks.CONTAMINATED_GRASS_BLOCK.get());
                        
                        // Dead Tree Blocks
                        output.accept(ModBlocks.DEAD_LOG.get());
                        output.accept(ModBlocks.DEAD_WOOD.get());
                        output.accept(ModBlocks.STRIPPED_DEAD_LOG.get());
                        output.accept(ModBlocks.STRIPPED_DEAD_WOOD.get());
=======
>>>>>>> 3598fac37cb55863843246fb1d6a25e626ceaf45
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    } 

}
