package com.wumple.megamap.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.wumple.megamap.Reference;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ModCommands
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal(Reference.MOD_ID).then(FillMegaMapCommand.register(dispatcher)));
	}

}