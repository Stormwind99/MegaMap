package com.wumple.megamap;

public class ClientProxy { }

/*
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
*/