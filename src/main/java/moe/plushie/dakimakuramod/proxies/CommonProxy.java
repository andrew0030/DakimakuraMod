package moe.plushie.dakimakuramod.proxies;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.dakimakura.DakiManager;
import moe.plushie.dakimakuramod.common.handler.SyncHandler;
import moe.plushie.dakimakuramod.common.network.GuiHandler;
import moe.plushie.dakimakuramod.common.network.PacketHandler;
import net.minecraft.server.MinecraftServer;

public class CommonProxy {
    
    private DakiManager dakimakuraManager;
    private ModBlocks modBlocks;
    
    public void preInit(FMLPreInitializationEvent event) {
        dakimakuraManager = new DakiManager(event.getSuggestedConfigurationFile().getParentFile().getParentFile());
        dakimakuraManager.loadPacks();
        modBlocks = new ModBlocks();
    }
    
    public void init(FMLInitializationEvent event) {
        modBlocks.registerTileEntities();
        new GuiHandler();
        PacketHandler.init();
        SyncHandler.init();
    }
    
    public void initRenderers() {
        DakimakuraMod.getLogger().error("Trying to init renderers on the server side.");
    }
    
    public void postInit(FMLPostInitializationEvent event) {
    }
    
    public ModBlocks getModBlocks() {
        return modBlocks;
    }
    
    public MinecraftServer getServer() {
        return MinecraftServer.getServer();
    }
    
    public DakiManager getDakimakuraManager() {
        return dakimakuraManager;
    }
}
