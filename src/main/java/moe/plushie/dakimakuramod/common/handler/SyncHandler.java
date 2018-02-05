package moe.plushie.dakimakuramod.common.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.network.PacketHandler;
import moe.plushie.dakimakuramod.common.network.message.server.MessageServerSendDakiList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class SyncHandler {
    
    public static SyncHandler INSTANCE;
    
    public static void init() {
        INSTANCE = new SyncHandler();
    }
    
    public SyncHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entity;
            DakimakuraMod.getLogger().info(String.format("Sending daki list to %s", player.getCommandSenderName()));
            PacketHandler.NETWORK_WRAPPER.sendTo(new MessageServerSendDakiList(), player);
        }
    }
}
