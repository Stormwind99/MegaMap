package com.wumple.megamap;

import com.wumple.megamap.megamap.ItemEmptyMegaMap;
import com.wumple.megamap.megamap.ItemMegaMap;
import com.wumple.util.misc.RegistrationHelpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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
        }
        
        public static void registerMoreOreNames()
        {
        }
        
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void registerRenders(ModelRegistryEvent event)
        {
        	RegistrationHelpers.registerRender(empty_megamap_item);
            RegistrationHelpers.registerRender(filled_megamap_item);
            
            // this doesn't work - leaves megamap_filled inventory not rendering
            ModelLoader.setCustomModelResourceLocation(filled_megamap_item, OreDictionary.WILDCARD_VALUE, new ModelResourceLocation(filled_megamap_item.getRegistryName(), "inventory"));
        }
        
        public static void init(FMLInitializationEvent event) 
        {
            if (event.getSide() == Side.CLIENT)
            {
            	// fixes megamap_filled inventory not rendering
                Minecraft mc = Minecraft.getMinecraft();
                RenderItem ri = mc.getRenderItem();
                ItemModelMesher imm = ri.getItemModelMesher();
                ResourceLocation name = ObjectHolder.filled_megamap_item.getRegistryName();
                imm.register(ObjectHolder.filled_megamap_item, new ItemMeshDefinition() {
                    @Override
                    public ModelResourceLocation getModelLocation(ItemStack stack) {
                        return new ModelResourceLocation(name, "inventory");
                    }
                });
            }
        }
    }
}