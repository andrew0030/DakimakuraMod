package com.github.andrew0030.dakimakuramod.netwok;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.netwok.client.CommandClientMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class DMNetwork
{
    public static final String NETWORK_PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(DakimakuraMod.MODID, "net"))
            .networkProtocolVersion(() -> NETWORK_PROTOCOL)
            .clientAcceptedVersions(NETWORK_PROTOCOL::equals)
            .serverAcceptedVersions(NETWORK_PROTOCOL::equals)
            .simpleChannel();

    /**
     * Used to set up all the Messages
     */
    public static void registerMessages()
    {
        int id = -1;
        //Client Messages
        CHANNEL.registerMessage(++id, CommandClientMessage.class, CommandClientMessage::serialize, CommandClientMessage::deserialize, CommandClientMessage::handle);
        //Server Messages
    }
}