package net.flame.flamesradiology;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.flame.flamesradiology.common.init.ModItems;
import net.flame.flamesradiology.common.init.ModBlocks;
import net.flame.flamesradiology.common.init.ModCreativeModeTabs;
import net.flame.flamesradiology.common.init.ModSounds;
import net.flame.flamesradiology.common.init.ModEffects;
import net.flame.flamesradiology.common.init.ModFluids;
import net.flame.flamesradiology.common.fluid.ModFluidTypes;
import net.flame.flamesradiology.registry.RadioactiveSourceRegistry;
import net.flame.flamesradiology.network.NetworkHandler;
import net.flame.flamesradiology.common.event.AxeStrippingHandler;
import net.minecraft.world.item.CreativeModeTabs;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(FlamesRadiology.MOD_ID)
public class FlamesRadiology {
    public static final String MOD_ID = "flames_radiology";
    public static final Logger LOGGER = LogUtils.getLogger();

    /* My Mod ID for my mod is flames_radiology*/

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public FlamesRadiology(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for mod loading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register items
        ModItems.register(modEventBus);
        
        // Register blocks
        ModBlocks.register(modEventBus);
        
        // Register creative mode tabs
        ModCreativeModeTabs.register(modEventBus);
        
        // Register sounds
        ModSounds.register(modEventBus);
        
        // Register effects
        ModEffects.register(modEventBus);
        
        // Register fluid types
        ModFluidTypes.register(modEventBus);
        
        // Register fluids
        ModFluids.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        
        // Register networking
        modEventBus.addListener(NetworkHandler::register);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Initialize radioactive sources from popular mods
        RadioactiveSourceRegistry.initializeDefaultSources();
        
        // Initialize axe stripping map
        AxeStrippingHandler.initStrippingMap();
        
        LOGGER.info("Registered radioactive sources for Flames Radiology");
    }

    // Add the geiger counter to the tools tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.GEIGER_COUNTER.get());
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
