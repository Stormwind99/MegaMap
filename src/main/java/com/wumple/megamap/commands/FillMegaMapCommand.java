package com.wumple.megamap.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.wumple.megamap.megamap.FilledMegaMapItem;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class FillMegaMapCommand implements Command<CommandSource>
{
	private static final FillMegaMapCommand CMD = new FillMegaMapCommand();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher)
    {
        return Commands.literal("fillmegamap")
                //.requires(cs -> cs.hasPermissionLevel(0))
                .executes(CMD);
    }
    
    public static <T> T Util_as(Object o, Class<T> t)
    {
        return t.isInstance(o) ? t.cast(o) : null;
    }
    
    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException 
    { 	
    	CommandSource source = context.getSource();
        World world = source.getWorld();
        
    	Entity entity = source.getEntity();
        ServerPlayerEntity player = source.asPlayer();
        
        ItemStack mapStack = null;
        FilledMegaMapItem mapItem = null;
        
        // get a held map
        if (player != null)
        {
            mapStack = player.getHeldItemMainhand();
            mapItem = (mapStack != null) ? Util_as(mapStack.getItem(), FilledMegaMapItem.class) : null;
            
            if (mapItem == null)
            {
                mapStack = player.getHeldItemOffhand();
                mapItem = (mapStack != null) ? Util_as(mapStack.getItem(), FilledMegaMapItem.class) : null;
            }
        }
        
        // get the map data
        MapData mapData = (mapStack != null) ? FilledMegaMapItem.getMapData(mapStack, world) : null;
        
        if (mapData == null) 
        {
        	context.getSource().sendErrorMessage(new TranslationTextComponent("command.megamap.fillmegamap.nomap"));
            return 0; 
        } 

        mapItem.fillMapData(world, entity, mapData);
        context.getSource().sendFeedback(new TranslationTextComponent("command.megamap.fillmegamap.filledmap"), true);
        
        return 0;
    }
}
