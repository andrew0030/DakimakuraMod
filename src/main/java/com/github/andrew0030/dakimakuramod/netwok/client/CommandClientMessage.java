package com.github.andrew0030.dakimakuramod.netwok.client;

import com.github.andrew0030.dakimakuramod.netwok.client.util.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record CommandClientMessage(CommandType commandType)
{
    public void serialize(FriendlyByteBuf buf)
    {
        buf.writeByte(this.commandType().ordinal());
    }

    public static CommandClientMessage deserialize(FriendlyByteBuf buf)
    {
        return new CommandClientMessage(CommandType.values()[buf.readByte()]);
    }

    public static void handle(CommandClientMessage message, Supplier<NetworkEvent.Context> ctx)
    {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT)
        {
            context.enqueueWork(() ->
            {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleCommandClient(message));
            });
            context.setPacketHandled(true);
        }
    }

    public enum CommandType
    {
        OPEN_PACK_FOLDER;
    }
}