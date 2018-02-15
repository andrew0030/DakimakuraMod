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
    int sizeFront = 0;
    int sizeBack = 0;
    private byte[] data;
    
    public MessageServerSendTextures() {}
    
    public MessageServerSendTextures(Daki daki, int sizeFront, int sizeBack, byte[] data) {
        this.daki = daki;
        this.sizeFront = sizeFront;
        this.sizeBack = sizeBack;
        this.data = data;
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, daki.getPackDirectoryName() + ":" + daki.getDakiDirectoryName());
        buf.writeInt(sizeFront);
        buf.writeInt(sizeBack);
        buf.writeBoolean(data != null);
        if (data != null) {
            buf.writeInt(data.length);
            buf.writeBytes(data);
        }
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        String path = ByteBufUtils.readUTF8String(buf);
        String[] pathSplit = path.split(":");
        daki = DakimakuraMod.getProxy().getDakimakuraManager().getDakiFromMap(pathSplit[0], pathSplit[1]);
        sizeFront = buf.readInt();
        sizeBack = buf.readInt();
        if (buf.readBoolean()) {
            int size = buf.readInt();
            data = new byte[size];
            buf.readBytes(data);
        }
    }

    @Override
    public IMessage onMessage(MessageServerSendTextures message, MessageContext ctx) {
        sendToClientManager(message.daki, message.sizeFront, message.sizeBack, message.data);
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    private void sendToClientManager(Daki daki, int sizeFront, int sizeBack, byte[] data) {
        DakiSendHelper.gotDakiTexturePartFromServer(daki, sizeFront, sizeBack, data);
    }
}
