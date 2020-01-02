package com.wumple.megamap.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.wumple.megamap.Reference;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> cmdTut = dispatcher.register(
                Commands.literal(Reference.MOD_ID)
                        .then(FillMegaMapCommand.register(dispatcher))
        );

        //dispatcher.register(Commands.literal("wumple").redirect(cmdTut));
    }

}