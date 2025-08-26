package net.flame.flamesradiology.common.init;

import net.flame.flamesradiology.FlamesRadiology;
import net.flame.flamesradiology.common.block.ContaminatedWaterBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(BuiltInRegistries.BLOCK, FlamesRadiology.MOD_ID);

    public static final DeferredHolder<Block, Block> BORON_BLOCK = registerBlock("boron_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredHolder<Block, Block> LEAD_BLOCK = registerBlock("lead_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredHolder<Block, Block> BORON_ORE = registerBlock("boron_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredHolder<Block, Block> LEAD_ORE = registerBlock("lead_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredHolder<Block, Block> POLY_HALITE_ORE = registerBlock("poly_halite_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredHolder<Block, Block> DEEPSLATE_BORON_ORE = registerBlock("deepslate_boron_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final DeferredHolder<Block, Block> DEEPSLATE_LEAD_ORE = registerBlock("deepslate_lead_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final DeferredHolder<Block, Block> DEEPSLATE_POLY_HALITE_ORE = registerBlock("deepslate_poly_halite_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final DeferredHolder<Block, Block> RAW_BORON_BLOCK = registerBlock("raw_boron_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredHolder<Block, Block> RAW_LEAD_BLOCK = registerBlock("raw_lead_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredHolder<Block, Block> RAW_POLY_HALITE_BLOCK = registerBlock("raw_poly_halite_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    // Contaminated Blocks
    public static final DeferredHolder<Block, Block> WASTE_LAND_DIRT = registerBlock("waste_land_dirt",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.GRAVEL)));

    public static final DeferredHolder<Block, Block> CONTAMINATED_GRASS_BLOCK = registerBlock("contaminated_grass_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.6f).sound(SoundType.GRASS)));

    // Dead Tree Blocks
    public static final DeferredHolder<Block, RotatedPillarBlock> DEAD_LOG = registerBlock("dead_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.WOOD)));

    public static final DeferredHolder<Block, RotatedPillarBlock> DEAD_WOOD = registerBlock("dead_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.WOOD)));

    public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_DEAD_LOG = registerBlock("stripped_dead_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.WOOD)));

    public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_DEAD_WOOD = registerBlock("stripped_dead_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.WOOD)));

    // Fluid Blocks
    public static final DeferredHolder<Block, LiquidBlock> CONTAMINATED_WATER_BLOCK = BLOCKS.register("contaminated_water_block",
            () -> new ContaminatedWaterBlock());
    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block){
        DeferredHolder<Block, T> toReturn = BLOCKS.register(name, block);
        registeBlockItem(name, toReturn);
        return toReturn;
    }

    public static <T extends Block> void registeBlockItem(String name, DeferredHolder<Block, T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
