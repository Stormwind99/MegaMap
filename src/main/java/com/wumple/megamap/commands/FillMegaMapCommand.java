package com.wumple.megamap.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.wumple.megamap.api.IFilledMegaMapItem;
import com.wumple.megamap.util.Util;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/*
 * fillmegamap debugging command
 * 
 * Warning: can be VERY slow on a map of scale >=5 and non-generated chunks!
 */
public class FillMegaMapCommand implements Command<CommandSource>
{
	private static final FillMegaMapCommand CMD = new FillMegaMapCommand();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher)
    {
        return Commands.literal("fillmegamap")
                //.requires(cs -> cs.hasPermissionLevel(0))
                .executes(CMD);
    }
    
    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException 
    { 	
    	CommandSource source = context.getSource();
        World world = source.getWorld();
        
    	Entity entity = source.getEntity();
        ServerPlayerEntity player = source.asPlayer();
        
        ItemStack mapStack = null;
        IFilledMegaMapItem mapItem = null;
        
        // get a held map
        if (player != null)
        {
            mapStack = player.getHeldItemMainhand();
            mapItem = (mapStack != null) ? Util.as(mapStack.getItem(), IFilledMegaMapItem.class) : null;
            
            if (mapItem == null)
            {
                mapStack = player.getHeldItemOffhand();
                mapItem = (mapStack != null) ? Util.as(mapStack.getItem(), IFilledMegaMapItem.class) : null;
            }
        }
        
        context.getSource().sendFeedback(new TranslationTextComponent("command.megamap.fillmegamap.filling"), true);
        boolean ret = mapItem.fillMapData(world, entity, mapStack);
        
        if (ret)
        	context.getSource().sendFeedback(new TranslationTextComponent("command.megamap.fillmegamap.filledmap"), true);
        else
        	context.getSource().sendErrorMessage(new TranslationTextComponent("command.megamap.fillmegamap.nomap"));

        
        return 0;
    }
}