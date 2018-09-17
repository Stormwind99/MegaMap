package com.wumple.megamap;

import java.util.ArrayList;
import java.util.List;

import com.wumple.megamap.megamap.ItemMegaMap;
import com.wumple.util.base.misc.Util;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class FillMegaMapCommand implements ICommand
{
    private final List<String> aliases;
    
    public FillMegaMapCommand()
    {
        aliases = new ArrayList<String>(); 
        aliases.add("fmm"); 
        aliases.add("fillmap");
        aliases.add("fm"); 
    }
    
    @Override
    public int compareTo(ICommand arg0)
    {
        return 0;
    }

    @Override
    public String getName()
    {
        return "fillmegamap";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "fillmegamap";
    }

    @Override
    public List<String> getAliases()
    {
        return this.aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        World world = sender.getEntityWorld(); 
                
        if (!world.isRemote) 
        { 
            Entity entity = sender.getCommandSenderEntity();
            EntityLivingBase living = Util.as(entity, EntityLivingBase.class);
            
            ItemStack mapStack = null;
            ItemMegaMap mapItem = null;
            
            if (living != null)
            {
                mapStack = living.getHeldItemMainhand();
                mapItem = (mapStack != null) ? Util.as(mapStack.getItem(), ItemMegaMap.class) : null;
                
                if (mapItem == null)
                {
                    mapStack = living.getHeldItemOffhand();
                    mapItem = (mapStack != null) ? Util.as(mapStack.getItem(), ItemMegaMap.class) : null;
                }
            }
            
            MapData mapData = (mapItem != null) ? mapItem.getMapData(mapStack, world) : null;
            
            if (mapData == null) 
            {
                sender.sendMessage(new TextComponentTranslation("command.megamap.fillmegamap.nomap"));
                return; 
            } 

            mapItem.fillMapData(world, entity, mapData);
            sender.sendMessage(new TextComponentTranslation("command.megamap.fillmegamap.filledmap"));
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        EntityPlayer player = Util.as(sender.getCommandSenderEntity(), EntityPlayer.class);
        if (player != null)
        {
            return player.isCreative();
        }
        
        return false;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
    {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return false;
    }

}
