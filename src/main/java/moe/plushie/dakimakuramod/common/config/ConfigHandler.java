package moe.plushie.dakimakuramod.common.config;

import java.io.File;

import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ConfigHandler {
    
    public static final String CATEGORY_RECIPE = "recipe";
    public static final String CATEGORY_LOOT = "loot";
    public static final String CATEGORY_CLIENT = "client";
    private static final String LANG_KEY_PREFIX = "config." + LibModInfo.ID + ":";
    
    public static Configuration config;
    
    // General
    public static boolean onlyUnlockNewSkins;
    
    // Recipes
    public static boolean enableRecipe;
    public static boolean useAltRecipe;
    public static boolean enableRecycleRecipe;
    public static boolean enableClearingRecipe;
    
    // Loot
    public static boolean addUnlockToLootChests;
    public static float mobDropChance;
    public static float mobDropLootingBonus;
    
    // Client
    public static int textureMaxSize;
    public static int dakiRenderDist;
    
    public ConfigHandler(File file) {
        if (config == null) {
            config = new Configuration(file, "1");
            loadConfigFile();
        }
        FMLCommonHandler.instance().bus().register(this);
    }
    
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.getModID().equals(LibModInfo.ID)) {
            loadConfigFile();
        }
    }

    public void loadConfigFile() {
        loadCategoryCommon();
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            loadCategoryClient();
        }
        if (config.hasChanged()) {
            config.save();
        }
    }
    
    private void loadCategoryCommon() {
        loadCategoryRecipe();
        loadCategoryLoot();
    }
    
    private void loadCategoryRecipe() {
        enableRecipe = config.getBoolean("enableRecipe", CATEGORY_RECIPE, true,
                "Enable the crafting recipe for dakimakuras.",
                LANG_KEY_PREFIX + "enableRecipe");
        
        useAltRecipe = config.getBoolean("useAltRecipe", CATEGORY_RECIPE, false,
                "Changes the crafting recipe from 6 wool to 3 wool and 6 string.",
                LANG_KEY_PREFIX + "useAltRecipe");
        
        enableRecycleRecipe = config.getBoolean("enableRecycleRecipe", CATEGORY_RECIPE, true,
                "Allow getting a new dakimakura design by crafting 2 unwanted designs together.",
                LANG_KEY_PREFIX + "enableRecycleRecipe");
        
        enableClearingRecipe = config.getBoolean("enableClearingRecipe", CATEGORY_RECIPE, true,
                "Allow crafting a dakimakura to clear its design.",
                LANG_KEY_PREFIX + "enableClearingRecipe"); 
    }
    
    private void loadCategoryLoot() {
        addUnlockToLootChests = config.getBoolean("addUnlockToLootChests", CATEGORY_LOOT, false,
                "Add the dakimakura design items to loot chests around the world.",
                LANG_KEY_PREFIX + "addUnlockToLootChests");
        
        mobDropChance = config.getFloat("mobDropChance", CATEGORY_LOOT, 1F, 0F, 100F,
                "Percentage chance of mobs dropping a dakimakura design. 0 disables mob drops.",
                LANG_KEY_PREFIX + "mobDropChance");
        
        mobDropLootingBonus = config.getFloat("mobDropLootingBonus", CATEGORY_LOOT, 1F, 0F, 100F,
                "Extra bonus percentage chance of mobs dropping a dakimakura design for each level of looting.\n"
                + "mobDropChance + (mobDropLootingBonus * lootingLevel)",
                LANG_KEY_PREFIX + "mobDropLootingBonus");
        
    }
    
    private void loadCategoryClient() {
        textureMaxSize = config.getInt("textureMaxSize", CATEGORY_CLIENT, 1024, 32, 8192,
                "Max texture size for dakimakuras.\n"
                + "This will be rounded up to the nearest power of 2.\n"
                + "Will be capped at the GPUs max texture size.",
                LANG_KEY_PREFIX + "textureMaxSize");
        
        dakiRenderDist = config.getInt("dakiRenderDist", CATEGORY_CLIENT, 64, 32, 256,
                "The maximum distance away in blocks dakimakuras will render.",
                LANG_KEY_PREFIX + "dakiRenderDist");
        dakiRenderDist = dakiRenderDist * dakiRenderDist;
    }
}
