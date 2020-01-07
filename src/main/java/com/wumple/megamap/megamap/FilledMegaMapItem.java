package com.wumple.megamap.megamap;

import com.wumple.megamap.ConfigManager;
import com.wumple.util.xmap.XFilledMapItem;

import net.minecraft.item.Item;

public class FilledMegaMapItem extends XFilledMapItem 
{
	public static final String ID = "megamap:megamap_filled";
	
	@Override
	public String getID()
	{
		return ID;
	}

	public FilledMegaMapItem(Item.Properties builder)
	{
		super(builder.maxStackSize(1));

		setRegistryName(ID);
	}

	// IXFilledMapItem

	@Override
	public boolean isAMapScaleValid(byte scale)
	{
		return isMapScaleValid(scale);
	}
	
	protected static boolean isMapScaleValid(byte scale)
	{
		return (scale >= 0) && (scale <= ConfigManager.General.maxScale.get());
	}
}
