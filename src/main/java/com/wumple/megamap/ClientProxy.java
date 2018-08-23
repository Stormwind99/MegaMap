package com.wumple.megamap;

import com.wumple.util.proxy.CombinedClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CombinedClientProxy
{

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        
        // fixes megamap_filled inventory not rendering
        Minecraft mc = Minecraft.getMinecraft();
        RenderItem ri = mc.getRenderItem();
        ItemModelMesher imm = ri.getItemModelMesher();
        ResourceLocation name = ObjectHolder.filled_megamap_item.getRegistryName();
        imm.register(ObjectHolder.filled_megamap_item, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                return new ModelResourceLocation(name, "inventory");
            }
        });

    }

}
