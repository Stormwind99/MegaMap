package com.wumple.megamap.recipes;

import com.wumple.megamap.Util;
import com.wumple.megamap.api.MegaMapAPI;
import com.wumple.megamap.megamap.FilledMegaMapItem;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class MegaMapExtendingRecipe extends ShapedRecipe
{
	public MegaMapExtendingRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn,
			NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn)
	{
		super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
	}

	/*
	  public MegaMapExtendingRecipe(ResourceLocation idIn) {
	      super(idIn, "", 3, 3, NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.FILLED_MAP), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.PAPER)), new ItemStack(Items.MAP));
	*/

	public boolean matches(CraftingInventory inv, World worldIn)
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
				if (MegaMapAPI.getInstance().isFilledMap(itemstack1))
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
				MapData mapdata = null;
				
				// use MegaMapItem if possible to get MegaMapData instead of just MapData
				if (itemstack.getItem() instanceof FilledMegaMapItem)
				{
					FilledMegaMapItem item = Util.as(itemstack.getItem(), FilledMegaMapItem.class);
					mapdata = item.getMyMapData(itemstack, worldIn);
				}
				// otherwise if normal map, fallback
				else
				{
					mapdata = FilledMapItem.getMapData(itemstack, worldIn);
				}
				
				if (mapdata == null)
				{
					return false;
				}
				// TODO else if (MegaMapAPI.getInstance().isExplorationMap(mapdata))
				else if (this.isExplorationMap(mapdata))
				{
					return false;
				}
				else
				{
                   byte testScale = (byte) (mapdata.scale + 1);
                   return MegaMapAPI.getInstance().isMapScaleValid(itemstack, testScale);
				}
			}
		}
	}

	private boolean isExplorationMap(MapData mapData)
	{
		if (mapData.mapDecorations != null)
		{
			for (MapDecoration mapdecoration : mapData.mapDecorations.values())
			{
				if (mapdecoration.getType() == MapDecoration.Type.MANSION
						|| mapdecoration.getType() == MapDecoration.Type.MONUMENT)
				{
					return true;
				}
			}
		}

		return false;
	}

	public ItemStack getCraftingResult(CraftingInventory inv)
	{
		ItemStack itemstack = ItemStack.EMPTY;

		for (int i = 0; i < inv.getSizeInventory() && itemstack.isEmpty(); ++i)
		{
			ItemStack itemstack1 = inv.getStackInSlot(i);
			if (MegaMapAPI.getInstance().isFilledMap(itemstack1))
			{
				itemstack = itemstack1;
			}
		}

		ItemStack newItemstack = itemstack.copy();
		newItemstack.setCount(1);
		newItemstack.getOrCreateTag().putInt("map_scale_direction", 1);
		return itemstack;
	}

	/**
	 * If true, this recipe does not appear in the recipe book and does not respect
	 * recipe unlocking (and the doLimitedCrafting gamerule)
	 */
	public boolean isDynamic()
	{
		return true;
	}
}
