package com.wumple.megamap;

import com.wumple.megamap.megamap.ItemEmptyMegaMap;
import com.wumple.megamap.megamap.ItemMegaMap;
import com.wumple.util.crafting.RecipeUtil;
import com.wumple.util.misc.RegistrationHelpers;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

@GameRegistry.ObjectHolder("megamap")
public class ObjectHolder
{
    // @GameRegistry.ObjectHolder("megamap:megamap_filled")
    public static /* final */ Item filled_megamap_item = null;

    // @GameRegistry.ObjectHolder("megamap:megamap_empty")
    public static /* final */ Item empty_megamap_item = null;

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
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

            filled_megamap_item = RegistrationHelpers.regHelperOre(registry, new ItemMegaMap(), Ids.mapFilled);
            empty_megamap_item = RegistrationHelpers.regHelperOre(registry, new ItemEmptyMegaMap(), Ids.mapEmpty);

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

        /**
         * Remove crafting recipes.
         *
         * @param event
         *            The event
         */
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void removeRecipes(final RegistryEvent.Register<IRecipe> event)
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