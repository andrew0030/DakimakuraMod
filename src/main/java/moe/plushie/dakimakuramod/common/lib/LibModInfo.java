package moe.plushie.dakimakuramod.common.lib;

public class LibModInfo {
    public static final String ID = "dakimakuramod";
    public static final String NAME = "Dakimakura Mod";
    public static final String VERSION = "@VERSION@";
    public static final String CHANNEL = "dakimod";
    public static final String UPDATE_URL = "http://plushie.moe/app_update/minecraft_mods/dakimakuramod/update.json";
    public static final String UPDATE_PAGE = "https://discord.gg/5Z3KKvU";
    public static final String DEPENDENY = "required:forge@[14.23.2.2638,);";
    
    public static final String PROXY_CLIENT_CLASS = "moe.plushie.dakimakuramod.proxies.ClientProxy";
    public static final String PROXY_COMMNON_CLASS = "moe.plushie.dakimakuramod.proxies.CommonProxy";
    public static final String GUI_FACTORY_CLASS = "moe.plushie.dakimakuramod.client.gui.ModGuiFactory";
    
    public static boolean isDevelopmentVersion() {
        return VERSION.startsWith("@VER");
    }
}
