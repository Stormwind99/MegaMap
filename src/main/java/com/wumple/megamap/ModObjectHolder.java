package com.wumple.megamap;

import com.wumple.megamap.megamap.FilledMegaMapItem;
import com.wumple.megamap.megamap.MegaMapItem;

import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModObjectHolder
{
    //@ObjectHolder("megamap:megamap_filled")
    public static FilledMegaMapItem filled_megamap_item;

    //@ObjectHolder("megamap:megamap_empty")
    public static MegaMapItem empty_megamap_item;
}