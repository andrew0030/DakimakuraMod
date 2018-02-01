package moe.plushie.dakimakuramod;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import moe.plushie.dakimakuramod.common.command.CommandDakimakura;
import moe.plushie.dakimakuramod.common.creativetab.CreativeTabDakimakura;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import moe.plushie.dakimakuramod.proxies.CommonProxy;

@Mod(modid = LibModInfo.ID, version = LibModInfo.VERSION)
public class DakimakuraMod {
    
    @Instance(LibModInfo.ID)
    public static DakimakuraMod instance;

    @SidedProxy(clientSide = LibModInfo.PROXY_CLIENT_CLASS, serverSide = LibModInfo.PROXY_COMMNON_CLASS)
    public static CommonProxy proxy;
    
    public static Logger logger;
    
    public static CreativeTabDakimakura creativeTabDakimakura = new CreativeTabDakimakura();
    
    @EventHandler
    public void perInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info(String.format("Loading %s version %s", LibModInfo.NAME, LibModInfo.VERSION));
        proxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        proxy.initRenderers();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandDakimakura());
        proxy.getDakimakuraManager().loadPacks();
    }
    
    public static CommonProxy getProxy() {
        return proxy;
    }
}
