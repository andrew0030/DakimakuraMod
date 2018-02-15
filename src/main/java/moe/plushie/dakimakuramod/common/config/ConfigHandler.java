package moe.plushie.dakimakuramod.common.config;

import java.io.File;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
    
    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_RECIPE = "recipe";
    private static final String CATEGORY_LOOT = "loot";
    private static final String CATEGORY_CLIENT = "client";
    
    public static Configuration config;
    
    // General
    public static boolean onlyUnlockNewSkins;
    
    
    // Recipes
    public static boolean enableRecipe;
    public static boolean useAltRecipe;
    
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

        loadCategoryCommon();
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            loadCategoryClient();
        }
        if (config.hasChanged()) {
            config.save();
        }
    }
    
    private static void loadCategoryCommon() {
        loadCategoryRecipe();
        loadCategoryLoot();
    }
    
    private static void loadCategoryRecipe() {
        enableRecipe = config.getBoolean("enableRecipe", CATEGORY_RECIPE, true,
                "Enable the crafting recipe for dakimakuras.");
        
        useAltRecipe = config.getBoolean("useAltRecipe", CATEGORY_RECIPE, false,
                "Changes the crafting recipe from 6 wool to 3 wool and 6 string.");
    }
    
    private static void loadCategoryLoot() {
        addUnlockToLootChests = config.getBoolean("addUnlockToLootChests", CATEGORY_LOOT, true,
                "Added the daki design items to loot chests around the world.");
    }
    
    private static void loadCategoryClient() {
        textureMaxSize = config.getInt("textureMaxSize", CATEGORY_CLIENT, 1024, 32, 8192,
                "Max texture size for the dakis.\n"
                + "This will be rounded up to the nearest power of 2.\n"
                + "Will be capped at the GPUs max texture size.");
        
        romajiName = config.getBoolean("romajiName", CATEGORY_CLIENT, true,
                "If true daki names will be displayed in romaji, false they will display their original name.");
    }
}
