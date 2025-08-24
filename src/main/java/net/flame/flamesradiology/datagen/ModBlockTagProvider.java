package net.flame.flamesradiology.datagen;

import net.flame.flamesradiology.FlamesRadiology;
import net.flame.flamesradiology.common.init.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, FlamesRadiology.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // All custom blocks are mineable with iron pickaxe
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(ModBlocks.BORON_BLOCK.get())
            .add(ModBlocks.LEAD_BLOCK.get())
            .add(ModBlocks.RAW_BORON_BLOCK.get())
            .add(ModBlocks.RAW_LEAD_BLOCK.get())
            .add(ModBlocks.RAW_POLY_HALITE_BLOCK.get())
            .add(ModBlocks.BORON_ORE.get())
            .add(ModBlocks.LEAD_ORE.get())
            .add(ModBlocks.POLY_HALITE_ORE.get())
            .add(ModBlocks.DEEPSLATE_BORON_ORE.get())
            .add(ModBlocks.DEEPSLATE_LEAD_ORE.get())
            .add(ModBlocks.DEEPSLATE_POLY_HALITE_ORE.get());

        // All custom blocks need iron tool level
        this.tag(BlockTags.NEEDS_IRON_TOOL)
            .add(ModBlocks.BORON_BLOCK.get())
            .add(ModBlocks.LEAD_BLOCK.get())
            .add(ModBlocks.RAW_BORON_BLOCK.get())
            .add(ModBlocks.RAW_LEAD_BLOCK.get())
            .add(ModBlocks.RAW_POLY_HALITE_BLOCK.get())
            .add(ModBlocks.BORON_ORE.get())
            .add(ModBlocks.LEAD_ORE.get())
            .add(ModBlocks.POLY_HALITE_ORE.get())
            .add(ModBlocks.DEEPSLATE_BORON_ORE.get())
            .add(ModBlocks.DEEPSLATE_LEAD_ORE.get())
            .add(ModBlocks.DEEPSLATE_POLY_HALITE_ORE.get());
    }
}
