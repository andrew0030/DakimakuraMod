package moe.plushie.dakimakuramod.common.handler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.network.PacketHandler;
import moe.plushie.dakimakuramod.common.network.message.server.MessageServerSendDakiList;
import net.minecraft.entity.player.EntityPlayerMP;

public class SyncHandler {
    
    public static SyncHandler INSTANCE;
    
    public static void init() {
        INSTANCE = new SyncHandler();
    }
    
    public SyncHandler() {
        FMLCommonHandler.instance().bus().register(this);
    }
    
    @SubscribeEvent
    public void onPlayerLoggedInEvent(PlayerLoggedInEvent  event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            DakimakuraMod.getLogger().info(String.format("Sending daki list to %s", player.getCommandSenderName()));
            PacketHandler.NETWORK_WRAPPER.sendTo(new MessageServerSendDakiList(), player);
        }
    }
}
