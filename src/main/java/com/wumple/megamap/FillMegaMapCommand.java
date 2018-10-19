package com.wumple.megamap;

import java.util.ArrayList;
import java.util.List;

import com.wumple.megamap.megamap.ItemMegaMap;
import com.wumple.util.base.misc.Util;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class FillMegaMapCommand extends CommandBase
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
    public int getRequiredPermissionLevel()
    {
        // TODO what do the level values mean?
        // Maybe https://minecraft.gamepedia.com/Server.properties
        return 4;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        EntityPlayer player = Util.as(sender.getCommandSenderEntity(), EntityPlayer.class);
        if (player != null)
        {
            //return super.checkPermission(server, sender);
            return player.isCreative();
        }
        
        return false;
    }
}
