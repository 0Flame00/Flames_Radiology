package net.flame.flamesradiology.common.init;

import net.flame.flamesradiology.FlamesRadiology;
import net.flame.flamesradiology.common.item.GeigerCounterItem;
import net.flame.flamesradiology.common.item.PotassiumIodideItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(BuiltInRegistries.ITEM, FlamesRadiology.MOD_ID);

    public static final DeferredHolder<Item, Item> GEIGER_COUNTER = ITEMS.register("geiger_counter",
        () -> new GeigerCounterItem(new Item.Properties()));

    public static final DeferredHolder<Item, Item> LEAD_INGOT = ITEMS.register("lead_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> LEAD_NUGGET = ITEMS.register("lead_nugget",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> BORON_INGOT = ITEMS.register("boron_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> BORON_NUGGET = ITEMS.register("boron_nugget",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> RAW_BORON = ITEMS.register("raw_boron",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> RAW_LEAD = ITEMS.register("raw_lead",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> POTASSIUM = ITEMS.register("potassium",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> RUBBER = ITEMS.register("rubber",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> ACTIVATED_CARBON = ITEMS.register("activated_carbon",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> GLASS_TUBE = ITEMS.register("glass_tube",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> FABRIC = ITEMS.register("fabric",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> GAS_FILTER = ITEMS.register("gas_filter",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> IODINE = ITEMS.register("iodine",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> POTASSIUM_IODIDE = ITEMS.register("potassium_iodide",
            () -> new PotassiumIodideItem(new Item.Properties().food(ModFoods.POTASSIUM_IODIDE)));

    // Hazmat Suit Armor
    public static final DeferredHolder<Item, ArmorItem> HAZMAT_HELMET = ITEMS.register("hazmat_helmet",
        () -> new ArmorItem(ModArmorMaterials.HAZMAT, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final DeferredHolder<Item, ArmorItem> HAZMAT_CHESTPLATE = ITEMS.register("hazmat_chestplate",
        () -> new ArmorItem(ModArmorMaterials.HAZMAT, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredHolder<Item, ArmorItem> HAZMAT_LEGGINGS = ITEMS.register("hazmat_leggings",
        () -> new ArmorItem(ModArmorMaterials.HAZMAT, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final DeferredHolder<Item, ArmorItem> HAZMAT_BOOTS = ITEMS.register("hazmat_boots",
        () -> new ArmorItem(ModArmorMaterials.HAZMAT, ArmorItem.Type.BOOTS, new Item.Properties()));

    // Netherite Hazmat Suit Armor
    public static final DeferredHolder<Item, ArmorItem> NETHERITE_HAZMAT_HELMET = ITEMS.register("netherite_hazmat_helmet",
        () -> new ArmorItem(ModArmorMaterials.NETHERITE_HAZMAT, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final DeferredHolder<Item, ArmorItem> NETHERITE_HAZMAT_CHESTPLATE = ITEMS.register("netherite_hazmat_chestplate",
        () -> new ArmorItem(ModArmorMaterials.NETHERITE_HAZMAT, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final DeferredHolder<Item, ArmorItem> NETHERITE_HAZMAT_LEGGINGS = ITEMS.register("netherite_hazmat_leggings",
        () -> new ArmorItem(ModArmorMaterials.NETHERITE_HAZMAT, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final DeferredHolder<Item, ArmorItem> NETHERITE_HAZMAT_BOOTS = ITEMS.register("netherite_hazmat_boots",
        () -> new ArmorItem(ModArmorMaterials.NETHERITE_HAZMAT, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
