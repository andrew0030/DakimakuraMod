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
import moe.plushie.dakimakuramod.common.dakimakura.pack.DakiPackFolder;
import moe.plushie.dakimakuramod.common.dakimakura.pack.DakiPackZipFile;
import moe.plushie.dakimakuramod.common.dakimakura.pack.IDakiPack;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiJsonSerializer;

public class MessageServerSendDakiList implements IMessage, IMessageHandler<MessageServerSendDakiList, IMessage> {

    private final DakiManager dakiManager;
    
    private ArrayList<IDakiPack> packs;
    
    public MessageServerSendDakiList() {
        dakiManager = DakimakuraMod.getProxy().getDakimakuraManager();
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        ArrayList<IDakiPack> dakiPacks = dakiManager.getDakiPacksList();
        DakimakuraMod.getLogger().info("Sending " + dakiPacks.size() + " packs to client.");
        buf.writeInt(dakiPacks.size());
        for (int i = 0; i < dakiPacks.size(); i++) {
            IDakiPack dakiPack = dakiPacks.get(i);
            buf.writeBoolean(dakiPack instanceof DakiPackFolder);
            ByteBufUtils.writeUTF8String(buf, dakiPack.getResourceName());
            ArrayList<Daki> dakis = dakiPack.getDakisInPack();
            //DakimakuraMod.getLogger().info("Sending " + dakis.size() + " dakis to client.");
            buf.writeInt(dakis.size());
            for (int j = 0; j < dakis.size(); j++) {
                Daki daki = dakis.get(j);
                ByteBufUtils.writeUTF8String(buf, daki.getDakiDirectoryName());
                ByteBufUtils.writeUTF8String(buf, DakiJsonSerializer.serialize(daki).toString());
            }
        }
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        packs = new ArrayList<IDakiPack>();
        int packCount = buf.readInt();
        DakimakuraMod.getLogger().info("Getting " + packCount + " packs from server.");
        for (int i = 0; i < packCount; i++) {
            boolean folder = buf.readBoolean();
            String packName = ByteBufUtils.readUTF8String(buf);
            IDakiPack dakiPack = null;
            if (folder) {
                dakiPack = new DakiPackFolder(packName);
            } else {
                dakiPack = new DakiPackZipFile(packName);
            }
            int dakiCount = buf.readInt();
            //DakimakuraMod.getLogger().info("Getting " + dakiCount + " dakis from server.");
            for (int j = 0; j < dakiCount; j++) {
                String path = ByteBufUtils.readUTF8String(buf);
                String dakiJson = ByteBufUtils.readUTF8String(buf);
                Daki daki = DakiJsonSerializer.deserialize(dakiJson, dakiPack.getResourceName(), path);
                dakiPack.addDaki(daki);
            }
            packs.add(dakiPack);
        }
    }

    @Override
    public IMessage onMessage(MessageServerSendDakiList message, MessageContext ctx) {
        DakimakuraMod.getProxy().setDakiList(message.packs);
        return null;
    }
}
