package moe.plushie.dakimakuramod.client.gui;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

@SideOnly(Side.CLIENT)
public class ModConfigGui extends GuiConfig {

    public ModConfigGui(GuiScreen parent) {
        super(parent, makeConfigScreens(), LibModInfo.ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
    }

    public static List<IConfigElement> makeConfigScreens() {
        List<IConfigElement> configs = new ArrayList<IConfigElement>();
        
        configs.addAll(new ConfigElement(ConfigHandler.config
                .getCategory(ConfigHandler.CATEGORY_RECIPE))
                .getChildElements());
        
        configs.addAll(new ConfigElement(ConfigHandler.config
                .getCategory(ConfigHandler.CATEGORY_LOOT))
                .getChildElements());
        
        configs.addAll(new ConfigElement(ConfigHandler.config
                .getCategory(ConfigHandler.CATEGORY_CLIENT))
                .getChildElements());
        
        configs.addAll(new ConfigElement(ConfigHandler.config
                .getCategory(ConfigHandler.CATEGORY_SERVER))
                .getChildElements());
        return configs;
    }
}
