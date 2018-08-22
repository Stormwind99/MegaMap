package com.wumple.megamap.recipes;

import com.wumple.megamap.ObjectHolder;
import com.wumple.megamap.megamap.MegaMapUtil;
import com.wumple.util.base.misc.Util;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class RecipesMegaMapExtending extends ShapedRecipes
{
    public RecipesMegaMapExtending()
    {
        super("", 3, 3, NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItem(ObjectHolder.filled_megamap_item), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER)), new ItemStack(ObjectHolder.filled_megamap_item));
    }
    
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        if (!super.matches(inv, worldIn))
        {
            return false;
        }
        else
        {
            ItemStack itemstack = ItemStack.EMPTY;

            for (int i = 0; i < inv.getSizeInventory() && itemstack.isEmpty(); ++i)
            {
                ItemStack itemstack1 = inv.getStackInSlot(i);

                if (MegaMapUtil.isFilledMap(itemstack1))
                {
                    itemstack = itemstack1;
                }
            }

            if (itemstack.isEmpty())
            {
                return false;
            }
            else
            {
            	ItemMap item = Util.as(itemstack.getItem(), ItemMap.class);
                MapData mapdata = item.getMapData(itemstack, worldIn);

                if (mapdata == null)
                {
                    return false;
                }
                else if (this.isExplorationMap(mapdata))
                {
                    return false;
                }
                else
                {
                    return MegaMapUtil.isMapScaleValid(itemstack, mapdata.scale);
                }
            }
        }
    }

    private boolean isExplorationMap(MapData p_190934_1_)
    {
        if (p_190934_1_.mapDecorations != null)
        {
            for (MapDecoration mapdecoration : p_190934_1_.mapDecorations.values())
            {
                if (mapdecoration.getType() == MapDecoration.Type.MANSION || mapdecoration.getType() == MapDecoration.Type.MONUMENT)
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack itemstack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory() && itemstack.isEmpty(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (MegaMapUtil.isFilledMap(itemstack1))
            {
                itemstack = itemstack1;
            }
        }

        itemstack = itemstack.copy();
        itemstack.setCount(1);

        if (itemstack.getTagCompound() == null)
        {
            itemstack.setTagCompound(new NBTTagCompound());
        }

        itemstack.getTagCompound().setInteger("map_scale_direction", 1);
        return itemstack;
    }

    /**
     * If true, this recipe does not appear in the recipe book and does not respect recipe unlocking (and the
     * doLimitedCrafting gamerule)
     */
    public boolean isDynamic()
    {
        return true;
    }
}
