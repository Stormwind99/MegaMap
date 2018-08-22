package com.wumple.megamap;

import com.wumple.megamap.megamap.ItemEmptyMegaMap;
import com.wumple.megamap.megamap.ItemMegaMap;
import com.wumple.util.misc.RegistrationHelpers;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event)
        {
            final IForgeRegistry<Item> registry = event.getRegistry();

            filled_megamap_item = RegistrationHelpers.regHelper(registry, new ItemMegaMap());
            empty_megamap_item = RegistrationHelpers.regHelper(registry, new ItemEmptyMegaMap());
            
            registerMoreOreNames();
            registerTileEntities();
        }
        
        public static void registerTileEntities()
        {
            //RegistrationHelpers.registerTileEntity(TileEntityPantograph.class, BlockPantograph.ID);
        }
        
        public static void registerMoreOreNames()
        {
            //OreDictionary.registerOre(Ids.listAllMetalIngots, Items.IRON_INGOT);
        }
        
        @SubscribeEvent
        public static void registerRenders(ModelRegistryEvent event)
        {
            RegistrationHelpers.registerRender(filled_megamap_item);
            RegistrationHelpers.registerRender(empty_megamap_item);
        }
    }
}