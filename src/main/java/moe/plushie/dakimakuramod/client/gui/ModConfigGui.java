package moe.plushie.dakimakuramod.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
        return configs;
    }
}
