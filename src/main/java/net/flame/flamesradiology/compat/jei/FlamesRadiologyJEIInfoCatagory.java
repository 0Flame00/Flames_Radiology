package net.flame.flamesradiology.compat.jei;

import javax.annotation.Nonnull;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.flame.flamesradiology.FlamesRadiology;
import net.minecraft.network.chat.Component;

public class FlamesRadiologyJEIInfoCatagory implements IRecipeCategory<FlamesRadiologyJEIInfoRecipe> {
    
    public static final RecipeType<FlamesRadiologyJEIInfoRecipe> RECIPE_TYPE = 
        RecipeType.create(FlamesRadiology.MOD_ID, "info", FlamesRadiologyJEIInfoRecipe.class);
    
    private final IDrawable background;
    private final IDrawable icon;
    
    public FlamesRadiologyJEIInfoCatagory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(160, 125);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, 
            new net.minecraft.world.item.ItemStack(net.flame.flamesradiology.common.init.ModItems.GEIGER_COUNTER.get()));
    }
    
    @Override
    public RecipeType<FlamesRadiologyJEIInfoRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }
    
    @Override
    public Component getTitle() {
        return Component.literal("Item Information");
    }
    
    @Override
<<<<<<< HEAD
    public IDrawable getBackground() {
        return background;
    }
    
    @Override
=======
>>>>>>> 3598fac37cb55863843246fb1d6a25e626ceaf45
    public IDrawable getIcon() {
        return icon;
    }
    
    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull FlamesRadiologyJEIInfoRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 72, 54)
            .addItemStack(recipe.getItem());
    }
}
