package com.wumple.megamap.api;

import java.util.function.BiFunction;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public interface IItemMegaMap
{
    boolean isAMapScaleValid(byte scale);
    ItemStack setupANewMap(World worldIn, double worldX, double worldZ, byte scale, boolean trackingPosition, boolean unlimitedTracking);
    public void fillMapData(World worldIn, Entity viewer, MapData data);
    public void updateMapData(World worldIn, Entity viewer, MapData data);
    public void updateMapDataArea(World worldIn, Entity viewer, MapData data, int startPixelX, int startPixelZ, int endPixelX, int endPixelZ, BiFunction<Integer, Integer, Boolean> usePixel);
    public String getID();
}