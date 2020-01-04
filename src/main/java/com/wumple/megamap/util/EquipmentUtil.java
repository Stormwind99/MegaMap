package com.wumple.megamap.util;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

public class EquipmentUtil
{
	public static <T> T findHeldItem(ServerPlayerEntity player, Class<T> t)
	{
		ItemStack stack;
		T item = null;
		
        // get a held map
        if (player != null)
        {
            stack = player.getHeldItemMainhand();
            item = (stack != null) ? Util.as(stack.getItem(), t) : null;
            
            if (item == null)
            {
            	stack = player.getHeldItemOffhand();
                item = (stack != null) ? Util.as(stack.getItem(), t) : null;
            }
        }

        return item;
	}
}
