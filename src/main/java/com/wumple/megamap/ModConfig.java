package com.wumple.megamap;

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

// See
// https://github.com/McJty/YouTubeModding14/blob/master/src/main/java/com/mcjty/mytutorial/Config.java
// https://wiki.mcjty.eu/modding/index.php?title=Tut14_Ep6

@Mod.EventBusSubscriber
public class ModConfig
{
	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
	private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

	public static ForgeConfigSpec COMMON_CONFIG;
	public static ForgeConfigSpec CLIENT_CONFIG;

	public static final String CATEGORY_GENERAL = "General";
	public static final String CATEGORY_DEBUGGING = "Debugging";

	public static class General
	{
		public static ForgeConfigSpec.IntValue defaultScale;
		public static ForgeConfigSpec.IntValue maxScale;
		public static ForgeConfigSpec.BooleanValue disableVanillaRecipes;
		
		public static int maxMaxScale = 127;

		private static void setupConfig()
		{
			COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);

			// @Name("Default scale")
			defaultScale = COMMON_BUILDER.comment("Map scale new mega maps start with by default")
					.defineInRange("defaultScale", 1, 0, maxMaxScale);

			// @Name("Max scale")
			maxScale = COMMON_BUILDER.comment("Maximum map scale for mega maps").defineInRange("maxScale", 10, 0, maxMaxScale);

			// @Name("Disable vanilla map recipes")
			// @RequiresMcRestart
			disableVanillaRecipes = COMMON_BUILDER
					.comment("Disable vanilla map recipes and replace with mega map recipes instead")
					.define("disableVanillaRecipes", true);

			COMMON_BUILDER.pop();
		}
	}

	public static class Debugging
	{
		public static ForgeConfigSpec.BooleanValue debug;
		public static ForgeConfigSpec.BooleanValue usePlaceholderTileEntity;

		private static void setupConfig()
		{
			// @Config.Comment("Debugging options")
			COMMON_BUILDER.comment("Debugging settings").push(CATEGORY_DEBUGGING);

			//@Name("Debug mode")
			debug = COMMON_BUILDER.comment("Enable general debug features, display extra debug info").define("debug",
					false);

			COMMON_BUILDER.pop();
		}
	}

	static
	{
		General.setupConfig();
		Debugging.setupConfig();

		COMMON_CONFIG = COMMON_BUILDER.build();
		CLIENT_CONFIG = CLIENT_BUILDER.build();
	}

	public static void loadConfig(ForgeConfigSpec spec, Path path)
	{

		final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave()
				.writingMode(WritingMode.REPLACE).build();

		configData.load();
		spec.setConfig(configData);
	}

	@SubscribeEvent
	public static void onLoad(final net.minecraftforge.fml.config.ModConfig.Loading configEvent)
	{
	}

	@SubscribeEvent
	public static void onReload(final net.minecraftforge.fml.config.ModConfig.ConfigReloading configEvent)
	{
	}

	public static void setupConfig()
	{
		ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, CLIENT_CONFIG);
		ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, COMMON_CONFIG);

		loadConfig(ModConfig.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Reference.MOD_ID + "-client.toml"));
		loadConfig(ModConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Reference.MOD_ID + "-common.toml"));
	}
}
