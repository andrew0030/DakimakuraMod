package com.github.andrew0030.dakimakuramod.commands;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.netwok.NetworkUtil;
import com.github.andrew0030.dakimakuramod.registries.DMCreativeTab;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class DakiCommand
{
    public static void createCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        String commandString = "dakimakura";
        String openPackFolderArg = "openPackFolder";
        String reloadArg = "reload";

        LiteralCommandNode<CommandSourceStack> source = dispatcher.register(Commands.literal(commandString)
//                 .requires((permission) -> permission.hasPermission(2)) //TODO add permission check to reload
                .then(Commands.literal(openPackFolderArg)
                .executes(cs -> {
                    Entity entity = cs.getSource().getEntity();
                    if (entity instanceof ServerPlayer serverPlayer)
                        NetworkUtil.openDakiFolder(serverPlayer);
                    return 1;
                }))
                .then(Commands.literal(reloadArg)
                .executes(cs -> {
                    DakimakuraMod.getDakimakuraManager().loadPacks(true);
                    DakimakuraMod.getTextureManagerCommon().clear();
                    DMCreativeTab.reloadTabContents();
                    return 1;
                }))
        );
        dispatcher.register(Commands.literal(commandString).redirect(source));
    }
}