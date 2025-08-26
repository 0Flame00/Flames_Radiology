package net.flame.flamesradiology.compat.jei;

import net.minecraft.world.item.ItemStack;
import java.util.List;

public class FlamesRadiologyJEIInfoRecipe {
    private final ItemStack item;
    private final List<String> description;
    
    public FlamesRadiologyJEIInfoRecipe(ItemStack item, List<String> description) {
        this.item = item;
        this.description = description;
    }
    
    public ItemStack getItem() {
        return item;
    }
    
    public List<String> getDescription() {
        return description;
    }
}
