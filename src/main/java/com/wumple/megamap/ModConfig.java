package com.wumple.megamap;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MOD_ID)
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ModConfig
{
    @Name("Default scale")
    @Config.Comment("The map scale new mega maps start with by default")
    public static byte defaultScale = 1;

    @Name("Max scale")
    @Config.Comment("The map scale maximum for mega maps")
    @RangeInt(min = 0, max = 127)
    public static byte maxScale = 10;

    @Name("Disable vanilla map recipes")
    @Config.Comment("Disable vanilla map recipes and replace with mega map recipes instead")
    @RequiresMcRestart
    public static boolean disableVanillaRecipes = true;

    @Name("Debugging")
    @Config.Comment("Debugging options")
    public static Debugging zdebugging = new Debugging();

    public static class Debugging
    {
        @Name("Debug mode")
        @Config.Comment("Enable debug features on this menu, display extra debug info.")
        public boolean debug = false;
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    private static class EventHandler
    {
        /**
         * Inject the new values and save to the config file when the config has been changed from the GUI.
         *
         * @param event
         *            The event
         */
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equals(Reference.MOD_ID))
            {
                ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
