package com.wumple.megamap.recipes;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.wumple.megamap.Reference;
import com.wumple.megamap.megamap.MegaMapUtil;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class MegaMapCloningRecipeFactory implements IRecipeFactory
{
    /**
     * hook for JSON to be able to use this recipe
     * 
     * @see _factories.json
     * @see filled_map_transcribe.json
     */
    @Override
    public IRecipe parse(JsonContext context, JsonObject json)
    {
        // TODO get rid of using ShapelessOreRecipe.factory to parse JSON
        ShapelessOreRecipe recipe = ShapelessOreRecipe.factory(context, json);

        return new MegaMapCloningRecipe(new ResourceLocation(Reference.MOD_ID, "megamap_cloning"), recipe.getRecipeOutput());
    }

    public static class MegaMapCloningRecipe extends ShapelessOreRecipe
    {
        public MegaMapCloningRecipe(ResourceLocation group, @Nonnull ItemStack result, Object... recipe)
        {
            super(group, result, recipe);
            // register for events so received onCrafting event can handle map transcription
            MinecraftForge.EVENT_BUS.register(this);
        }

        /**
         * Used to check if a recipe matches current crafting inventory
         */
        public boolean matches(InventoryCrafting inv, World worldIn)
        {
            int i = 0;
            ItemStack itemstack = ItemStack.EMPTY;

            for (int j = 0; j < inv.getSizeInventory(); ++j)
            {
                ItemStack itemstack1 = inv.getStackInSlot(j);

                if (!itemstack1.isEmpty())
                {
                    if (MegaMapUtil.isFilledMap(itemstack1))
                    {
                        if (!itemstack.isEmpty())
                        {
                            return false;
                        }

                        itemstack = itemstack1;
                    }
                    else
                    {
                        if (!MegaMapUtil.isEmptyMap(itemstack1))
                        {
                            return false;
                        }

                        ++i;
                    }
                }
            }

            return !itemstack.isEmpty() && i > 0;
        }

        /**
         * Returns an Item that is the result of this recipe
         */
        public ItemStack getCraftingResult(InventoryCrafting inv)
        {
            int i = 0;
            ItemStack itemstack = ItemStack.EMPTY;

            for (int j = 0; j < inv.getSizeInventory(); ++j)
            {
                ItemStack itemstack1 = inv.getStackInSlot(j);

                if (!itemstack1.isEmpty())
                {
                    if (MegaMapUtil.isFilledMap(itemstack1))
                    {
                        if (!itemstack.isEmpty())
                        {
                            return ItemStack.EMPTY;
                        }

                        itemstack = itemstack1;
                    }
                    else
                    {
                        if (!MegaMapUtil.isEmptyMap(itemstack1))
                        {
                            return ItemStack.EMPTY;
                        }

                        ++i;
                    }
                }
            }

            if (!itemstack.isEmpty() && i >= 1)
            {
                return MegaMapUtil.copyMap(itemstack, i);
            }
            else
            {
                return ItemStack.EMPTY;
            }
        }

        /**
         * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one possible result (e.g. it's dynamic and depends on its
         * inputs), then return an empty stack.
         */
        public ItemStack getRecipeOutput()
        {
            return ItemStack.EMPTY;
        }

        public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
        {
            NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack> withSize(inv.getSizeInventory(), ItemStack.EMPTY);

            for (int i = 0; i < nonnulllist.size(); ++i)
            {
                ItemStack itemstack = inv.getStackInSlot(i);
                nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
            }

            return nonnulllist;
        }

        /**
         * If true, this recipe does not appear in the recipe book and does not respect recipe unlocking (and the doLimitedCrafting gamerule)
         */
        public boolean isDynamic()
        {
            return true;
        }

        /**
         * Used to determine if this recipe can fit in a grid of the given width/height
         */
        public boolean canFit(int width, int height)
        {
            return width >= 3 && height >= 3;
        }
    }
}