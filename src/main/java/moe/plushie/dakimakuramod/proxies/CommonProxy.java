package moe.plushie.dakimakuramod.proxies;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.handler.SyncHandler;
import moe.plushie.dakimakuramod.common.items.ModItems;
import moe.plushie.dakimakuramod.common.network.GuiHandler;
import moe.plushie.dakimakuramod.common.network.PacketHandler;
import net.minecraft.server.MinecraftServer;

public class CommonProxy {
    
    private ModItems modItems;
    private ModBlocks modBlocks;
    
    public void preInit(FMLPreInitializationEvent event) {
        //modItems = new ModItems();
        modBlocks = new ModBlocks();
        //EntityRegistry.registerModEntity(EntityDakimakura.class, "dakimakura", 1, DakimakuraMod.instance, 10, 200, false);
    }
    
    public void init(FMLInitializationEvent event) {
        modBlocks.registerTileEntities();
        new GuiHandler();
        PacketHandler.init();
        SyncHandler.init();
    }
    
    public void initRenderers() {
        DakimakuraMod.logger.error("Trying to init renderers on the server side.");
    }
    
    public void postInit(FMLPostInitializationEvent event) {
    }
    
    public ModItems getModItems() {
        return modItems;
    }
    
    public ModBlocks getModBlocks() {
        return modBlocks;
    }
    
    public MinecraftServer getServer() {
        return MinecraftServer.getServer();
    }
}
