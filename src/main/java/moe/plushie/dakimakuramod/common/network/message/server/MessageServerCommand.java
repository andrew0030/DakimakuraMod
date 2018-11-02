package moe.plushie.dakimakuramod.common.network.message.server;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.lwjgl.Sys;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import moe.plushie.dakimakuramod.DakimakuraMod;
import net.minecraft.util.Util;

public class MessageServerCommand implements IMessage, IMessageHandler<MessageServerCommand, IMessage> {
    
    private ServerCommandType commandType;
    
    public MessageServerCommand() {
    }
    
    public MessageServerCommand(ServerCommandType commandType) {
        this.commandType = commandType;
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(commandType.ordinal());
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        commandType = ServerCommandType.values()[buf.readByte()];
    }
    
    @Override
    public IMessage onMessage(MessageServerCommand message, MessageContext ctx) {
        switch (message.commandType) {
        case OPEN_PACK_FOLDER:
            openPackFolder();
            break;
        }
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    private void openPackFolder() {
        File packFolder = DakimakuraMod.getProxy().getDakimakuraManager().getPackFolder();
        String packPath = packFolder.getAbsolutePath();

        if (Util.getOSType() == Util.EnumOS.OSX) {
            try {
                Runtime.getRuntime().exec(new String[] {"/usr/bin/open", packPath});
                return;
            } catch (IOException ioexception1) {
                DakimakuraMod.getLogger().error("Couldn\'t open file: " + ioexception1);
            }
        } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
            String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] {packPath});
            try {
                Runtime.getRuntime().exec(s1);
                return;
            } catch (IOException ioexception) {
                DakimakuraMod.getLogger().error("Couldn\'t open file: " + ioexception);
            }
        }

        boolean openedFailed = false;

        try {
            Class oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
            oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {packFolder.toURI()});
        } catch (Throwable throwable) {
            DakimakuraMod.getLogger().error("Couldn\'t open link: " + throwable);
            openedFailed = true;
        }

        if (openedFailed) {
            DakimakuraMod.getLogger().error("Opening via system class!");
            Sys.openURL("file://" + packPath);
        }
}
    
    public static enum ServerCommandType {
        OPEN_PACK_FOLDER;
    }
}
