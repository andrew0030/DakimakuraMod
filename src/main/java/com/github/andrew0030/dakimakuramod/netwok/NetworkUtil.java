package com.github.andrew0030.dakimakuramod.netwok;

import com.github.andrew0030.dakimakuramod.netwok.client.CommandClientMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class NetworkUtil
{
    public static void openDakiFolder(ServerPlayer serverPlayer)
    {
        DMNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new CommandClientMessage(CommandClientMessage.CommandType.OPEN_PACK_FOLDER));
    }
}