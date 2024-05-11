package com.github.andrew0030.dakimakuramod.netwok;

import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.netwok.client.CommandClientMessage;
import com.github.andrew0030.dakimakuramod.netwok.client.SendDakiListClientMessage;
import com.github.andrew0030.dakimakuramod.netwok.client.SendTexturesClientMessage;
import com.github.andrew0030.dakimakuramod.netwok.server.RequestTexturesServerMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class NetworkUtil
{
    public static void openDakiFolder(ServerPlayer serverPlayer)
    {
        DMNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new CommandClientMessage(CommandClientMessage.CommandType.OPEN_PACK_FOLDER));
    }

    public static void sendTextures(ServerPlayer serverPlayer, Daki daki, int sizeFront, int sizeBack, byte[] data)
    {
        DMNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SendTexturesClientMessage(daki, sizeFront, sizeBack, data));
    }

    public static void sendDakiList()
    {
        DMNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new SendDakiListClientMessage());
    }

    public static void sendDakiList(ServerPlayer serverPlayer)
    {
        DMNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SendDakiListClientMessage());
    }

    public static void clientRequestTextures(Daki daki)
    {
        DMNetwork.CHANNEL.sendToServer(new RequestTexturesServerMessage(daki));
    }
}