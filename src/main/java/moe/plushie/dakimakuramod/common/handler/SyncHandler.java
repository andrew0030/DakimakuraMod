package moe.plushie.dakimakuramod.common.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import moe.plushie.dakimakuramod.DakimakuraMod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
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
    public void onEntityConstructing(EntityConstructing event) {
        if (event.entity instanceof EntityPlayerMP) {
            DakimakuraMod.logger.info("onEntityConstructing");
        }
    }
    
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayerMP) {
            DakimakuraMod.logger.info("onEntityJoinWorld");
            // TODO Send daki list to players
        }
    }
}
