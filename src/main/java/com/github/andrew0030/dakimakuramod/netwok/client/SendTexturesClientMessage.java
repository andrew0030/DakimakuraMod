package com.github.andrew0030.dakimakuramod.netwok.client;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.netwok.client.util.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SendTexturesClientMessage(Daki daki, int sizeFront, int sizeBack, int packetsNeeded, int idx, byte[] data)
{
    public void serialize(FriendlyByteBuf buf)
    {
        buf.writeUtf(daki.getPackDirectoryName() + ":" + daki.getDakiDirectoryName());
        buf.writeInt(sizeFront);
        buf.writeInt(sizeBack);
        buf.writeInt(packetsNeeded);
        buf.writeInt(idx);
        buf.writeBoolean(data != null);
        if (data != null)
        {
            buf.writeInt(data.length);
            buf.writeBytes(data);
        }
    }

    public static SendTexturesClientMessage deserialize(FriendlyByteBuf buf)
    {
        String path = buf.readUtf(Short.MAX_VALUE);
        String[] pathSplit = path.split(":");
        Daki daki = DakimakuraMod.getDakimakuraManager().getDakiFromMap(pathSplit[0], pathSplit[1]);
        int sizeFront = buf.readInt();
        int sizeBack = buf.readInt();
        int packetsNeeded = buf.readInt();
        int idx = buf.readInt();
        byte[] data = new byte[0];
        if (buf.readBoolean())
        {
            int size = buf.readInt();
            data = new byte[size];
            buf.readBytes(data);
        }
        return new SendTexturesClientMessage(daki, sizeFront, sizeBack, packetsNeeded, idx, data);
    }

    public static void handle(SendTexturesClientMessage message, Supplier<NetworkEvent.Context> ctx)
    {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT)
        {
            context.enqueueWork(() ->
            {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleSendTextures(message));
            });
            context.setPacketHandled(true);
        }
    }
}