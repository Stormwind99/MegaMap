package com.wumple.megamap.api;

import net.minecraft.nbt.NBTTagCompound;

public interface IMegaMapData
{
    public void readFromNBT(NBTTagCompound nbt);
}