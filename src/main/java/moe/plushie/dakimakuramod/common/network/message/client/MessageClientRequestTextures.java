package moe.plushie.dakimakuramod.common.network.message.client;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;

public class MessageClientRequestTextures implements IMessage, IMessageHandler<MessageClientRequestTextures, IMessage> {

    private Daki daki;
    
    public MessageClientRequestTextures() {}
    
    public MessageClientRequestTextures(Daki daki) {
        this.daki = daki;
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, daki.getPackDirectoryName() + ":" + daki.getDakiDirectoryName());
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        String path = ByteBufUtils.readUTF8String(buf);
        String[] pathSplit = path.split(":");
        daki = DakimakuraMod.getProxy().getDakimakuraManager().getDakiFromMap(pathSplit[0], pathSplit[1]);
    }
    
    @Override
    public IMessage onMessage(MessageClientRequestTextures message, MessageContext ctx) {
        DakimakuraMod.getProxy().getTextureManagerCommon().onClientRequestTexture(ctx.getServerHandler().playerEntity, message.daki);
        return null;
    }
}
