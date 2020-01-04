package com.wumple.megamap.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class EquipmentUtil
{
	public static <T> T findHeldItemOf(LivingEntity entity, Class<T> t)
	{
		ItemStack stack;
		T item = null;
		
        // get a held map
        if (entity != null)
        {
            stack = entity.getHeldItemMainhand();
            item = (stack != null) ? Util.as(stack.getItem(), t) : null;
            
            if (item == null)
            {
            	stack = entity.getHeldItemOffhand();
                item = (stack != null) ? Util.as(stack.getItem(), t) : null;
            }
        }

        return item;
	}
}
