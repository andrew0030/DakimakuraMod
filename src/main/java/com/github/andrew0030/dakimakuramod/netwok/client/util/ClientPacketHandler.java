package com.github.andrew0030.dakimakuramod.netwok.client.util;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.netwok.client.CommandClientMessage;
import net.minecraft.Util;

public class ClientPacketHandler
{
    public static void handleCommandClient(CommandClientMessage msg)
    {
        CommandClientMessage.CommandType commandType = msg.commandType();
        if (commandType.equals(CommandClientMessage.CommandType.OPEN_PACK_FOLDER))
            Util.getPlatform().openUri(DakimakuraMod.getDakimakuraManager().getPackFolder().toURI());
    }
}