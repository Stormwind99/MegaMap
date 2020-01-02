package com.wumple.megamap;

import com.wumple.megamap.megamap.FilledMegaMapItem;
import com.wumple.megamap.megamap.MegaMapItem;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModObjectHolder
{
    //@ObjectHolder("megamap:megamap_filled")
    public static FilledMegaMapItem filled_megamap_item;

    //@ObjectHolder("megamap:megamap_empty")
    public static MegaMapItem empty_megamap_item;
}

/*
@EventHandler
public class ObjectHolder
{
    // @GameRegistry.ObjectHolder("megamap:megamap_filled")
    public static Item filled_megamap_item = null;  // final?

    // @GameRegistry.ObjectHolder("megamap:megamap_empty")
    public static Item empty_megamap_item = null; // final?
    
    @Mod.EventBusSubscriber(Reference.MOD_ID)
    public static class RegistrationHandler
    {
        public static class Ids
        {
            public static String[] mapFilled = { "mapAll", "mapFilled" };
            public static String[] mapEmpty = { "mapAll", "mapEmpty" };
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event)
        {
            final IForgeRegistry<Item> registry = event.getRegistry();

            filled_megamap_item = RegistrationHelpers.regHelperOre(registry, new FilledMegaMapItem(), Ids.mapFilled);
            empty_megamap_item = RegistrationHelpers.regHelperOre(registry, new MegaMapItem(), Ids.mapEmpty);

            registerTileEntities();
            registerMoreOreNames();
        }

        public static void registerTileEntities()
        {
        }

        public static void registerMoreOreNames()
        {
            RegistrationHelpers.registerOreNames(Items.MAP, Ids.mapEmpty);
            RegistrationHelpers.registerOreNames(new ItemStack(Items.FILLED_MAP, 1, OreDictionary.WILDCARD_VALUE), Ids.mapFilled);
            RegistrationHelpers.registerOreNames(new ItemStack(filled_megamap_item, 1, OreDictionary.WILDCARD_VALUE), Ids.mapFilled);
        }

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void registerRenders(ModelRegistryEvent event)
        {
            RegistrationHelpers.registerRender(empty_megamap_item);
            RegistrationHelpers.registerRender(filled_megamap_item);

            // this doesn't work - leaves megamap_filled inventory not rendering
            ModelLoader.setCustomModelResourceLocation(filled_megamap_item, OreDictionary.WILDCARD_VALUE,
                    new ModelResourceLocation(filled_megamap_item.getRegistryName(), "inventory"));
        }
        */

        /**
         * Remove crafting recipes.
         *
         * @param event
         *            The event
         */
        /*
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void removeRecipes() // final RegistryEvent.Register<IRecipe> event)
        {
            if (ModConfig.disableVanillaRecipes)
            {
                RegistrationHelpers.cheat(() -> {
                    // RecipeUtil.removeRecipes(RecipesMapCloning.class);
                    // RecipeUtil.removeRecipes(RecipesMapExtending.class);
                    RecipeUtil.removeRecipes(Items.MAP);
                    RecipeUtil.removeRecipes(Items.FILLED_MAP);
                } );
            }
        }
       
    }
}
*/