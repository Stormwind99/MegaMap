package com.wumple.megamap.recipes;

import com.wumple.megamap.MegaMap;
import com.wumple.megamap.megamap.MegaMapAPI;
import com.wumple.util.crafting.ShapedRecipe;
import com.wumple.util.xmap.XMapAPI;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class MegaMapExtendingRecipe extends ShapedRecipe
{
	public MegaMapExtendingRecipe(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn,
			NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn)
	{
		super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
	}

	@Override
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
				MapData mapdata = MegaMapAPI.getInstance().getMapData(itemstack, worldIn);

				if (mapdata == null)
				{
					return false;
				}
				else if (MegaMapAPI.getInstance().isExplorationMap(mapdata))
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

	@Override
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

		ItemStack newItemstack = ItemStack.EMPTY;

		if (itemstack != ItemStack.EMPTY)
		{
			newItemstack = MegaMapAPI.getInstance().copyMap(itemstack, 1);
			MegaMapAPI.getInstance().mapScaleDirection(newItemstack, 1);
		}

		return newItemstack;
	}

	/**
	 * If true, this recipe does not appear in the recipe book and does not respect
	 * recipe unlocking (and the doLimitedCrafting gamerule)
	 */
	@Override
	public boolean isDynamic()
	{
		return true;
	}

	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return MegaMap.CRAFTING_SPECIAL_MAPEXTENDING;
	}
}
