package com.wumple.megamap;

public class ModConfig
{
    // @Name("Default scale")
    // @Config.Comment("The map scale new mega maps start with by default")
    public static byte defaultScale = 1;

    // @Name("Max scale")
    // @Config.Comment("The map scale maximum for mega maps")
    // @RangeInt(min = 0, max = 127)
    public static byte maxScale = 10;

    // @Name("Disable vanilla map recipes")
    // @Config.Comment("Disable vanilla map recipes and replace with mega map recipes instead")
    // @RequiresMcRestart
    public static boolean disableVanillaRecipes = true;

    // @Name("Debugging")
    // @Config.Comment("Debugging options")
    //public static Debugging zdebugging = new Debugging();

    public static class Debugging
    {
        // @Name("Debug mode")
        // @Config.Comment("Enable debug features on this menu, display extra debug info.")
        public static boolean debug = false;
    }
}
