package com.wumple.megamap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wumple.megamap.commands.ModCommands;
import com.wumple.megamap.megamap.FilledMegaMapItem;
import com.wumple.megamap.megamap.MegaMapItem;
import com.wumple.megamap.recipes.MegaMapCloningRecipe;
import com.wumple.megamap.recipes.MegaMapExtendingRecipe;
import com.wumple.megamap.util.RecipeRemover;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.MapCloningRecipe;
import net.minecraft.item.crafting.MapExtendingRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class MegaMap	 /* extends ModBase */
{
	public Logger getLogger()
	{
		return LogManager.getLogger(Reference.MOD_ID);
	}

	public MegaMap()
	{
		ConfigManager.register(ModLoadingContext.get());

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addGenericListener(IRecipeSerializer.class, this::registerRecipeSerializers);
		modEventBus.addGenericListener(Item.class, this::registerItems);
		
		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onFingerprintViolation(final FMLFingerprintViolationEvent event)
	{
		getLogger().warn("Invalid fingerprint detected! The file " + event.getSource().getName()
				+ " may have been tampered with. This version will NOT be supported by the author!");
		getLogger().warn("Expected " + event.getExpectedFingerprint() + " found " + event.getFingerprints().toString());
	}

	@SubscribeEvent
	public void serverLoad(FMLServerStartingEvent event)
	{
		ModCommands.register(event.getCommandDispatcher());
	}

	@SubscribeEvent
	public void onServerStarted(FMLServerStartedEvent event)
	{
		if (ConfigManager.General.disableVanillaRecipes.get() == true)
		{
			final RecipeManager recipeManager = event.getServer().getRecipeManager();
			RecipeRemover.removeRecipes(recipeManager, MapCloningRecipe.class);
			RecipeRemover.removeRecipes(recipeManager, MapExtendingRecipe.class);
			RecipeRemover.removeRecipes(recipeManager, new ResourceLocation("minecraft", "map"));
			RecipeRemover.removeRecipes(recipeManager, new ResourceLocation("minecraft", "filled_map"));
		}
	}

	public static SpecialRecipeSerializer<MegaMapCloningRecipe> CRAFTING_SPECIAL_MAPCLONING;
	public static MegaMapExtendingRecipe.Serializer<MegaMapExtendingRecipe> CRAFTING_SPECIAL_MAPEXTENDING;

	private void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event)
	{
		CRAFTING_SPECIAL_MAPCLONING = new SpecialRecipeSerializer<>(MegaMapCloningRecipe::new);
		CRAFTING_SPECIAL_MAPCLONING.setRegistryName("megamap_cloning");
		CRAFTING_SPECIAL_MAPEXTENDING = new MegaMapExtendingRecipe.Serializer<MegaMapExtendingRecipe>(
				MegaMapExtendingRecipe::new);
		CRAFTING_SPECIAL_MAPEXTENDING.setRegistryName("megamap_extending");
		event.getRegistry().registerAll(CRAFTING_SPECIAL_MAPCLONING, CRAFTING_SPECIAL_MAPEXTENDING);
	}

	private void registerItems(RegistryEvent.Register<Item> event)
	{
		Item.Properties properties = new Item.Properties();

		ModObjectHolder.empty_megamap_item = new MegaMapItem(properties);
		ModObjectHolder.filled_megamap_item = new FilledMegaMapItem(properties);

		event.getRegistry().register(ModObjectHolder.empty_megamap_item);
		event.getRegistry().register(ModObjectHolder.filled_megamap_item);
	}
}
