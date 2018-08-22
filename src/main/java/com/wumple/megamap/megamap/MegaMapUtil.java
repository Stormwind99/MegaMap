package com.wumple.megamap.megamap;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class MegaMapUtil {

	public static boolean isEmptyMap(ItemStack itemstack1)
	{
		return itemstack1.getItem() == Items.MAP;
	}
	
    public static boolean isFilledMap(ItemStack itemstack1)
    {
    	return (itemstack1.getItem() == Items.FILLED_MAP)
    			|| (itemstack1.getItem() instanceof ItemMegaMap);
    }
    
    public static boolean isMapScaleValid(ItemStack itemstack, byte scale)
    {
    	// byte range is -128 to 127
    	if (itemstack.getItem() instanceof ItemMegaMap)
    	{
    		return (scale > -4) && (scale < 127);
    	}
    	else
    	{
    		return (scale < 4);
    	}
    }
    
    public static ItemStack copyMap(ItemStack itemstack, int i)
    {
    	 ItemStack itemstack2 = new ItemStack(itemstack.getItem(), i + 1, itemstack.getMetadata());

         if (itemstack.hasDisplayName())
         {
             itemstack2.setStackDisplayName(itemstack.getDisplayName());
         }

         if (itemstack.hasTagCompound())
         {
             itemstack2.setTagCompound(itemstack.getTagCompound());
         }

         return itemstack2;
    }
    
}
