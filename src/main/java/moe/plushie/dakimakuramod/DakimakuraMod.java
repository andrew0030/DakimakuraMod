package moe.plushie.dakimakuramod;

import org.apache.logging.log4j.Logger;

import moe.plushie.dakimakuramod.common.command.CommandDakimakura;
import moe.plushie.dakimakuramod.common.creativetab.CreativeTabDakimakura;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import moe.plushie.dakimakuramod.proxies.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = LibModInfo.ID, version = LibModInfo.VERSION, guiFactory = LibModInfo.GUI_FACTORY_CLASS, updateJSON = LibModInfo.UPDATE_URL, dependencies = LibModInfo.DEPENDENY)
public class DakimakuraMod {
    
    @Instance(LibModInfo.ID)
    private static DakimakuraMod instance;

    @SidedProxy(clientSide = LibModInfo.PROXY_CLIENT_CLASS, serverSide = LibModInfo.PROXY_COMMNON_CLASS)
    private static CommonProxy proxy;
    
    private static Logger logger;
    
    public static CreativeTabDakimakura creativeTabDakimakura = new CreativeTabDakimakura();
    
    @EventHandler
    public void perInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info(String.format("Loading %s version %s", LibModInfo.NAME, LibModInfo.VERSION));
        proxy.preInit(event);
        proxy.preInitRenderers();
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
        proxy.getDakimakuraManager().loadPacks(false);
        proxy.getTextureManagerCommon().serverStarted();
    }
    
    @EventHandler
    public void serverStop(FMLServerStoppingEvent event) {
        proxy.getTextureManagerCommon().serverStopped();
    }
    
    public static CommonProxy getProxy() {
        return proxy;
    }
    
    public static DakimakuraMod getInstance() {
        return instance;
    }
    
    public static Logger getLogger() {
        return logger;
    }
}
