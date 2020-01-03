package com.wumple.megamap.recipes;

import com.wumple.megamap.MegaMap;
import com.wumple.megamap.api.MegaMapAPI;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MegaMapCloningRecipe extends SpecialRecipe
{
	public MegaMapCloningRecipe(ResourceLocation idIn)
	{
		super(idIn);
	}

	public boolean matches(CraftingInventory inv, World worldIn)
	{
		int i = 0;
		ItemStack itemstack = ItemStack.EMPTY;

		for (int j = 0; j < inv.getSizeInventory(); ++j)
		{
			ItemStack itemstack1 = inv.getStackInSlot(j);
			if (!itemstack1.isEmpty())
			{
				if (MegaMapAPI.getInstance().isFilledMap(itemstack1))
				{
					if (!itemstack.isEmpty())
					{
						return false;
					}

					itemstack = itemstack1;
				}
				else
				{
					if (!MegaMapAPI.getInstance().isEmptyMap(itemstack1))
					{
						return false;
					}

					++i;
				}
			}
		}

		return !itemstack.isEmpty() && i > 0;
	}

	public ItemStack getCraftingResult(CraftingInventory inv)
	{
		int i = 0;
		ItemStack itemstack = ItemStack.EMPTY;

		for (int j = 0; j < inv.getSizeInventory(); ++j)
		{
			ItemStack itemstack1 = inv.getStackInSlot(j);
			if (!itemstack1.isEmpty())
			{
				if (MegaMapAPI.getInstance().isFilledMap(itemstack1))
				{
					if (!itemstack.isEmpty())
					{
						return ItemStack.EMPTY;
					}

					itemstack = itemstack1;
				}
				else
				{
					if (!MegaMapAPI.getInstance().isEmptyMap(itemstack1))
					{
						return ItemStack.EMPTY;
					}

					++i;
				}
			}
		}

		if (!itemstack.isEmpty() && i >= 1)
		{
			return MegaMapAPI.getInstance().copyMap(itemstack, i+1);
		}
		else
		{
			return ItemStack.EMPTY;
		}
	}

	/**
	 * Used to determine if this recipe can fit in a grid of the given width/height
	 */
	public boolean canFit(int width, int height)
	{
		return width >= 3 && height >= 3;
	}

	public IRecipeSerializer<?> getSerializer()
	{
		//return new SpecialRecipeSerializer<>(MapCloningRecipe::new);
		return MegaMap.CRAFTING_SPECIAL_MAPCLONING;
	}
}
