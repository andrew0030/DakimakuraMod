package moe.plushie.dakimakuramod.common.config;

import java.io.File;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
    
    public static String CATEGORY_GENERAL = "general";
    public static String CATEGORY_RECIPE = "recipe";
    public static String CATEGORY_LOOT = "loot";
    public static String CATEGORY_CLIENT = "client";
    
    public static Configuration config;
    
    // General
    public static boolean onlyUnlockNewSkins;
    
    
    // Recipes
    
    
    // Loot
    public static boolean addUnlockToLootChests;
    
    // Client
    public static int textureMaxSize;
    //public static boolean textureSample;
    //public static int dakiRenderDist;
    public static boolean romajiName;
    
    public static void init(File file) {
        if (config == null) {
            config = new Configuration(file, "1");
            loadConfigFile();
        }
    }

    public static void loadConfigFile() {
        loadCategoryGeneral();
        loadCategoryRecipe();
        loadCategoryLoot();
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            loadCategoryClient();
        }
        if (config.hasChanged()) {
            config.save();
        }
    }
    
    private static void loadCategoryGeneral() {
        
    }
    
    private static void loadCategoryRecipe() {
        
    }
    
    private static void loadCategoryLoot() {
        addUnlockToLootChests = config.getBoolean("addUnlockToLootChests", CATEGORY_LOOT, true,
                "Added the daki pattern items to loot chests around the world.");
    }
    
    private static void loadCategoryClient() {
        textureMaxSize = config.getInt("textureMaxSize", CATEGORY_CLIENT, 512, 8, Integer.MAX_VALUE,
                "Max texture size for the dakis.\n"
                + "This will be rounded up to the nearest power of 2.\n"
                + "Will be capped at the GPUs max texture size.");
        
        romajiName = config.getBoolean("romajiName", CATEGORY_CLIENT, true,
                "If true daki names will be displayed in romaji, false they will display their original name.");
    }
}
