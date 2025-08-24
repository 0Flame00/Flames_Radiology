package net.flame.flamesradiology.datagen;

import net.flame.flamesradiology.common.init.ModBlocks;
import net.flame.flamesradiology.common.init.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        HolderLookup.RegistryLookup<Enchantment> enchantmentLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        
        // Blocks that drop themselves
        this.dropSelf(ModBlocks.BORON_BLOCK.get());
        this.dropSelf(ModBlocks.LEAD_BLOCK.get());
        this.dropSelf(ModBlocks.RAW_BORON_BLOCK.get());
        this.dropSelf(ModBlocks.RAW_LEAD_BLOCK.get());
        
        // Raw poly halite block drops 9 potassium
        this.add(ModBlocks.RAW_POLY_HALITE_BLOCK.get(),
            block -> createSingleItemTable(ModItems.POTASSIUM.get(), UniformGenerator.between(9.0f, 9.0f)));
        
        // Ore blocks that drop raw materials with experience and fortune bonus
        this.add(ModBlocks.BORON_ORE.get(),
            block -> createOreDrop(ModBlocks.BORON_ORE.get(), ModItems.RAW_BORON.get(), enchantmentLookup));
        this.add(ModBlocks.DEEPSLATE_BORON_ORE.get(),
            block -> createOreDrop(ModBlocks.DEEPSLATE_BORON_ORE.get(), ModItems.RAW_BORON.get(), enchantmentLookup));
            
        this.add(ModBlocks.LEAD_ORE.get(),
            block -> createOreDrop(ModBlocks.LEAD_ORE.get(), ModItems.RAW_LEAD.get(), enchantmentLookup));
        this.add(ModBlocks.DEEPSLATE_LEAD_ORE.get(),
            block -> createOreDrop(ModBlocks.DEEPSLATE_LEAD_ORE.get(), ModItems.RAW_LEAD.get(), enchantmentLookup));
            
        this.add(ModBlocks.POLY_HALITE_ORE.get(),
            block -> createOreDrop(ModBlocks.POLY_HALITE_ORE.get(), ModItems.POTASSIUM.get(), enchantmentLookup));
        this.add(ModBlocks.DEEPSLATE_POLY_HALITE_ORE.get(),
            block -> createOreDrop(ModBlocks.DEEPSLATE_POLY_HALITE_ORE.get(), ModItems.POTASSIUM.get(), enchantmentLookup));
    }

    protected LootTable.Builder createOreDrop(Block block, Item item, HolderLookup.RegistryLookup<Enchantment> enchantmentLookup) {
        Holder<Enchantment> fortune = enchantmentLookup.getOrThrow(Enchantments.FORTUNE);
        return this.createSilkTouchDispatchTable(block,
            this.applyExplosionDecay(block,
                LootItem.lootTableItem(item)
                    .apply(ApplyBonusCount.addOreBonusCount(fortune))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(holder -> (Block) holder.get()).collect(java.util.stream.Collectors.toList());
    }
}
