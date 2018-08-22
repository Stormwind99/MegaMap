package com.wumple.megamap.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import com.wumple.megamap.recipes.DummyRecipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

// from https://github.com/Choonster-Minecraft-Mods/TestMod3/blob/1.12.2/src/main/java/choonster/testmod3/init/ModRecipes.java
public class RecipeUtil {
	
	protected static void log(String msg)
	{
	}
	
	/**
	 * Remove all crafting recipes with the specified {@link Block} as their output.
	 *
	 * @param output The output Block
	 */
	public static void removeRecipes(final Block output) {
		removeRecipes(Item.getItemFromBlock(output));
	}

	/**
	 * Remove all crafting recipes with the specified {@link Item} as their output.
	 *
	 * @param output The output Item
	 */
	public static void removeRecipes(final Item output) {
		final int recipesRemoved = removeRecipes(recipe -> {
			final ItemStack recipeOutput = recipe.getRecipeOutput();
			return !recipeOutput.isEmpty() && recipeOutput.getItem() == output;
		});

		log("Removed " + recipesRemoved + " recipe(s) for " + output.getRegistryName());
	}

	/**
	 * Remove all crafting recipes that are instances of the specified class.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforge.net/forum/index.php/topic,33631.0.html
	 *
	 * @param recipeClass The recipe class
	 */
	public static void removeRecipes(final Class<? extends IRecipe> recipeClass) {
		final int recipesRemoved = removeRecipes(recipeClass::isInstance);

		log("Removed " + recipesRemoved + " recipe(s) for " + recipeClass);
	}

	/**
	 * Remove all crafting recipes that match the specified predicate.
	 *
	 * @param predicate The predicate
	 * @return The number of recipes removed
	 */
	public static int removeRecipes(final Predicate<IRecipe> predicate) {
		int recipesRemoved = 0;

		final IForgeRegistry<IRecipe> registry = ForgeRegistries.RECIPES;
		final List<IRecipe> toRemove = new ArrayList<>();

		for (final IRecipe recipe : registry) {
			if (predicate.test(recipe)) {
				toRemove.add(recipe);
				recipesRemoved++;
			}
		}

		log("Overriding recipes with dummy recipes, please ignore any following \"Dangerous alternative prefix\" warnings.");
		toRemove.forEach(recipe -> {
			final ResourceLocation registryName = Objects.requireNonNull(recipe.getRegistryName());
			final IRecipe replacement = new DummyRecipe().setRegistryName(registryName);
			registry.register(replacement);
		});

		return recipesRemoved;
	}
}
