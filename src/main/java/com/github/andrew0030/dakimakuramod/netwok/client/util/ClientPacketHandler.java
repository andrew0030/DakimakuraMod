package com.github.andrew0030.dakimakuramod.netwok.client.util;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.DakimakuraModClient;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.DakiSendHelper;
import com.github.andrew0030.dakimakuramod.netwok.client.CommandClientMessage;
import com.github.andrew0030.dakimakuramod.netwok.client.SendDakiListClientMessage;
import com.github.andrew0030.dakimakuramod.netwok.client.SendTexturesClientMessage;
import net.minecraft.Util;

public class ClientPacketHandler
{
    public static void handleCommandClient(CommandClientMessage msg)
    {
        CommandClientMessage.CommandType commandType = msg.commandType();
        if (commandType.equals(CommandClientMessage.CommandType.OPEN_PACK_FOLDER))
            Util.getPlatform().openUri(DakimakuraMod.getDakimakuraManager().getPackFolder().toURI());
    }

    public static void handleSendTextures(SendTexturesClientMessage msg)
    {
        Daki daki = msg.daki();
        int sizeFront = msg.sizeFront();
        int sizeBack = msg.sizeBack();
        byte[] data = msg.data();
        DakiSendHelper.gotDakiTexturePartFromServer(daki, sizeFront, sizeBack, data);
    }

    public static void handleSendDakiList(SendDakiListClientMessage msg)
    {
        DakimakuraMod.getDakimakuraManager().setDakiList(msg.packs);
        DakimakuraModClient.getDakiTextureManager().reloadTextures();
    }
}