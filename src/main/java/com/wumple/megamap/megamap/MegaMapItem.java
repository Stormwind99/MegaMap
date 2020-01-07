package com.wumple.megamap.megamap;

import com.wumple.util.xmap.XMapItem;

import net.minecraft.item.Item;

public class MegaMapItem extends XMapItem 
{
	public static final String ID = "megamap:megamap_empty";

	public MegaMapItem(Item.Properties builder)
	{
		super(builder.maxStackSize(64));

		setRegistryName(ID);
	}

}