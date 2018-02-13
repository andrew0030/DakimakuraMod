package moe.plushie.dakimakuramod.common.network.message.server;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.DakiSendHelper;

public class MessageServerSendTextures implements IMessage, IMessageHandler<MessageServerSendTextures, IMessage> {

    private Daki daki;
    int firstSize;
    private byte[] data;
    
    public MessageServerSendTextures() {}
    
    public MessageServerSendTextures(Daki daki, int firstSize, byte[] data) {
        this.daki = daki;
        this.firstSize = firstSize;
        this.data = data;
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        //DakimakuraMod.getLogger().info("Sending texture to client.");
        ByteBufUtils.writeUTF8String(buf, daki.getPackDirectoryName() + ":" + daki.getDakiDirectoryName());
        buf.writeInt(data.length);
        buf.writeInt(firstSize);
        buf.writeBytes(data);
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        //DakimakuraMod.getLogger().info("Got texture from server.");
        String path = ByteBufUtils.readUTF8String(buf);
        String[] pathSplit = path.split(":");
        daki = DakimakuraMod.getProxy().getDakimakuraManager().getDakiFromMap(pathSplit[0], pathSplit[1]);
        int size = buf.readInt();
        firstSize = buf.readInt();
        data = new byte[size];
        buf.readBytes(data);
    }

    @Override
    public IMessage onMessage(MessageServerSendTextures message, MessageContext ctx) {
        sendToClientManager(message.daki, message.firstSize, message.data);
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    private void sendToClientManager(Daki daki, int firstSize, byte[] data) {
        DakiSendHelper.gotDakiTexturePartFromServer(daki, firstSize, data);
    }
}
