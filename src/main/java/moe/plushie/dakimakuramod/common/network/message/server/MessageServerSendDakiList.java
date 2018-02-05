package moe.plushie.dakimakuramod.common.network.message.server;

import java.util.ArrayList;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.DakiManager;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiJsonSerializer;

public class MessageServerSendDakiList implements IMessage, IMessageHandler<MessageServerSendDakiList, IMessage> {

    private final DakiManager dakiManager;
    private ArrayList<Daki> dakiList;
    
    public MessageServerSendDakiList() {
        dakiManager = DakimakuraMod.getProxy().getDakimakuraManager();
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        ArrayList<Daki> dakiList = dakiManager.getDakiList();
        buf.writeInt(dakiList.size());
        DakimakuraMod.getLogger().info("Sending " + dakiList.size() + " to client.");
        for (int i = 0; i < dakiList.size(); i++) {
            Daki daki = dakiList.get(i);
            ByteBufUtils.writeUTF8String(buf, daki.getPackDirectoryName() + ":" + daki.getDakiDirectoryName());
            ByteBufUtils.writeUTF8String(buf, DakiJsonSerializer.serialize(daki).toString());
        }
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        dakiList = new ArrayList<Daki>();
        int listSize = buf.readInt();
        DakimakuraMod.getLogger().info("Getting " + listSize + " from server.");
        for (int i = 0; i < listSize; i++) {
            String path = ByteBufUtils.readUTF8String(buf);
            String dakiJson = ByteBufUtils.readUTF8String(buf);
            String[] pathSplit = path.split(":");
            dakiList.add(DakiJsonSerializer.deserialize(dakiJson, pathSplit[0], pathSplit[1]));
        }
    }

    @Override
    public IMessage onMessage(MessageServerSendDakiList message, MessageContext ctx) {
        dakiManager.setDakiList(message.dakiList);
        return null;
    }
}
