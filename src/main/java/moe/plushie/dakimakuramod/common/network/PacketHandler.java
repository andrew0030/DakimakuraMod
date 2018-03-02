package moe.plushie.dakimakuramod.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import moe.plushie.dakimakuramod.common.network.message.client.MessageClientRequestTextures;
import moe.plushie.dakimakuramod.common.network.message.server.MessageServerSendDakiList;
import moe.plushie.dakimakuramod.common.network.message.server.MessageServerSendTextures;

public class PacketHandler {

    public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(LibModInfo.CHANNEL);
    private static int packetId = 0;
    
    public static void init() {
        registerMessage(MessageServerSendDakiList.class, MessageServerSendDakiList.class, Side.CLIENT);
        registerMessage(MessageClientRequestTextures.class, MessageClientRequestTextures.class, Side.SERVER);
        registerMessage(MessageServerSendTextures.class, MessageServerSendTextures.class, Side.CLIENT);
    }
    
    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
        NETWORK_WRAPPER.registerMessage(messageHandler, requestMessageType, packetId, side);
        packetId++;
    }
}
